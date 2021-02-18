package com.mustafaeren.planmaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class EventsFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ArrayList<String> davetEdilenEventIDList;

    private ArrayList<String> eventNameList;
    private ArrayList<String> eventAciklamaList;
    private ArrayList<String> eventTarihList;
    private ArrayList<String> eventSaatList;

    private ArrayList<String> davetlilerinDurumu;

    private eventGoruntulemeRecyclerAdapter eventGoruntulemeRecyclerAdapterXX;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragmen_events,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();




        davetEdilenEventIDList = new ArrayList<>();
        davetlilerinDurumu = new ArrayList<>();

        eventAciklamaList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventSaatList = new ArrayList<>();
        eventTarihList = new ArrayList<>();

        RecyclerView recyclerView = viewGroup.findViewById(R.id.davetList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventGoruntulemeRecyclerAdapterXX = new eventGoruntulemeRecyclerAdapter(eventNameList,eventAciklamaList,eventTarihList,eventSaatList, davetEdilenEventIDList);
        recyclerView.setAdapter(eventGoruntulemeRecyclerAdapterXX);







        CollectionReference collectionReference = firebaseFirestore.collection("Event");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null)
                {


                    eventNameList.clear();
                    davetEdilenEventIDList.clear();
                    eventAciklamaList.clear();
                    eventTarihList.clear();
                    eventSaatList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments())
                    {
                        String id = snapshot.getId();


                        documentReferenceFonk( id);
                    }

                }
            }
        });


        return viewGroup;
    }

    public void documentReferenceFonk(String id)
    {
        final DocumentReference documentReference = firebaseFirestore.collection("Event").document(id);
        final String id2 = id;
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!= null && value.exists())
                {

                    Map<String, Object> mapData = value.getData();
                    String id = value.getId();


                    String eventAciklama = (String) mapData.get("eventAciklama");
                    String eventName = (String) mapData.get("eventAdi");
                    String eventTarih = (String) mapData.get("eventTarih");
                    String eventSaat = (String) mapData.get("eventSaat");

                    davetliler(documentReference, eventName, eventAciklama,eventTarih,eventSaat,id);
                }
            }
        });
    }

    public void davetliler(DocumentReference documentReference, final String eventName, final String eventAciklama, final String eventTarih, final String eventSaat,final String id)
    {
        CollectionReference collectionReference = documentReference.collection("davetliler");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null)
                {


                    for(DocumentChange snapshot : value.getDocumentChanges()) {

                        Map<String, Object> data = snapshot.getDocument().getData();

                        if(snapshot.getDocument().getId().matches(firebaseAuth.getCurrentUser().getEmail()))
                        {

                            davetEdilenEventIDList.add(id);

                            eventNameList.add(eventName);
                            eventAciklamaList.add(eventAciklama);
                            eventTarihList.add(eventTarih);
                            eventSaatList.add(eventSaat);

                            eventGoruntulemeRecyclerAdapterXX.notifyDataSetChanged();

                        }


                    }

                }
            }
        });

    }

}
