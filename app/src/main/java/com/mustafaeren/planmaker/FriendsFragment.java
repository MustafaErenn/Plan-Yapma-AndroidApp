package com.mustafaeren.planmaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

public class FriendsFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> friendEmailFromFB;
    ArrayList<String> friendKullaniciAdiFromFb;
    arkadasGoruntuleRecyclerAdapter arkadasGoruntuleRecyclerAdapterXX;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        friendEmailFromFB = new ArrayList<>();
        friendKullaniciAdiFromFb = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragmen_friends,container,false);
        getFriendList();


        RecyclerView recyclerView = viewGroup.findViewById(R.id.friendList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arkadasGoruntuleRecyclerAdapterXX = new arkadasGoruntuleRecyclerAdapter(friendKullaniciAdiFromFb,friendEmailFromFB);
        recyclerView.setAdapter(arkadasGoruntuleRecyclerAdapterXX);

        return viewGroup;

    }

    public void getFriendList()
    {
        CollectionReference collectionReference = firebaseFirestore.collection("Friends");

        collectionReference.whereEqualTo("hesap",firebaseAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Toast.makeText(getActivity(), error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value!=null)
                {

                    friendEmailFromFB.clear();
                    friendKullaniciAdiFromFb.clear();
                    boolean forAGirdimi=false;

                    for(DocumentSnapshot snapshot : value.getDocuments())
                    {
                        forAGirdimi=true;
                        Map<String,Object> friendData = snapshot.getData();

                        String friendEmail = (String) friendData.get("arkadaş");


                        CollectionReference collectionReference2 = firebaseFirestore.collection("UserProfile");
                        collectionReference2.whereEqualTo("userEmail",friendEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value!=null)
                                {

                                    for(DocumentSnapshot snapshot : value.getDocuments())
                                    {

                                        Map<String,Object> data  = snapshot.getData();
                                        String kullaniciAdi = (String) data.get("kullaniciAdi");
                                        String email = (String) data.get("userEmail");

                                        friendEmailFromFB.add(email);
                                        friendKullaniciAdiFromFb.add(kullaniciAdi);

                                        arkadasGoruntuleRecyclerAdapterXX.notifyDataSetChanged();

                                    }
                                }
                            }
                        });


                    }

                    if(forAGirdimi==false)
                    {


                        arkadasGoruntuleRecyclerAdapterXX.notifyDataSetChanged();
                    }
                }

            }
        });
    }


}
