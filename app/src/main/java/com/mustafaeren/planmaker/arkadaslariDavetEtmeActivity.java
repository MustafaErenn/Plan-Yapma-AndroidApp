package com.mustafaeren.planmaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class arkadaslariDavetEtmeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> eventFriendEmailFromFB;
    ArrayList<String> eventFriendKullaniciAdiFromFb;
    arkadasDavetRecyclerAdapter arkadasDavetRecyclerAdapterXX;
    String intentEventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arkadaslari_davet_etme);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        eventFriendEmailFromFB = new ArrayList<>();
        eventFriendKullaniciAdiFromFb = new ArrayList<>();


        intentEventName = getIntent().getStringExtra("eventName");


        getFriendList();

        RecyclerView recyclerView = findViewById(R.id.arkadaslaraDavet_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arkadasDavetRecyclerAdapterXX = new arkadasDavetRecyclerAdapter(eventFriendKullaniciAdiFromFb,eventFriendEmailFromFB, intentEventName);
        recyclerView.setAdapter(arkadasDavetRecyclerAdapterXX);

    }

    public void getFriendList() {
        CollectionReference collectionReference = firebaseFirestore.collection("Friends");

        collectionReference.whereEqualTo("hesap", firebaseAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(arkadaslariDavetEtmeActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    eventFriendEmailFromFB.clear();
                    eventFriendKullaniciAdiFromFb.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> friendData = snapshot.getData();

                        String friendEmail = (String) friendData.get("arkadaş");


                        CollectionReference collectionReference2 = firebaseFirestore.collection("UserProfile");
                        collectionReference2.whereEqualTo("userEmail", friendEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {


                                    for (DocumentSnapshot snapshot : task.getResult()) {

                                        Map<String, Object> data = snapshot.getData();
                                        String kullaniciAdi = (String) data.get("kullaniciAdi");
                                        String email = (String) data.get("userEmail");

                                        eventFriendEmailFromFB.add(email);
                                        eventFriendKullaniciAdiFromFb.add(kullaniciAdi);

                                        arkadasDavetRecyclerAdapterXX.notifyDataSetChanged();

                                    }
                                }

                            }
                        });


                    }
                }
            }
        });
    }

    public void tamamlaButonu(View view)
    {

        HashMap<String,Object> eventData = new HashMap<>();
        eventData.put("status","Daveti Düzenleyen");

        firebaseFirestore.collection("Event").document(firebaseAuth.getCurrentUser().getEmail()+" "+intentEventName   ).
                collection("davetliler").document(firebaseAuth.getCurrentUser().getEmail()).
                set(eventData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(arkadaslariDavetEtmeActivity.this,"Davet işlemi tamamlandı",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(arkadaslariDavetEtmeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });
    }
}