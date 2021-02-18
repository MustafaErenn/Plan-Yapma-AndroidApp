package com.mustafaeren.planmaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventDurumlariGoruntuleActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String currentEventId;
    String currentUserEmail;

    ArrayList<String> davetlilerMailList;
    ArrayList<String> davetlilerDurumList;
    ArrayList<String> davetlilerKullaniciList;
    EditText durumEditText;

    eventDurumlariGoruntulemeAdapter eventDurumlariGoruntulemeAdapterXX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_durumlari_goruntule);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        durumEditText = findViewById(R.id.durumEditText);
        davetlilerDurumList = new ArrayList<>();
        davetlilerMailList = new ArrayList<>();
        davetlilerKullaniciList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        currentEventId = getIntent().getStringExtra("eventId");
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

        RecyclerView recyclerView = findViewById(R.id.arkadaslarinDurumu_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventDurumlariGoruntuleActivity.this));
        eventDurumlariGoruntulemeAdapterXX = new eventDurumlariGoruntulemeAdapter(davetlilerMailList,davetlilerDurumList,davetlilerKullaniciList);
        recyclerView.setAdapter(eventDurumlariGoruntulemeAdapterXX);



        CollectionReference collectionReference = firebaseFirestore.collection("Event").document(currentEventId).
                collection("davetliler");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error!=null)
                {
                    Toast.makeText(EventDurumlariGoruntuleActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if (value != null) {


                    davetlilerKullaniciList.clear();
                    davetlilerDurumList.clear();
                    davetlilerMailList.clear();
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        String id = snapshot.getId();

                        String CurrentId = id;

                        eventDurumlariAdapteraGidicekFonk1(id);

                    }
                }
            }
        });


    }
    public void eventDurumlariAdapteraGidicekFonk1(final String id)
    {
        DocumentReference documentReference = firebaseFirestore.collection("Event").document(currentEventId).
                collection("davetliler").document(id);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                if(value!=null && value.exists())
                {

                    Map<String,Object> statusData= value.getData();

                    String status =(String) statusData.get("status");

                    String finalStatus = status;
                    eventDurumlariAdapteraGidicekFonk2(id,status);
                }

            }
        });

    }
    public void eventDurumlariAdapteraGidicekFonk2(final String CurrentId,final String finalStatus)
    {
        CollectionReference collectionReference1 = firebaseFirestore.collection("UserProfile");

        collectionReference1.whereEqualTo("userEmail",CurrentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null)
                {






                    for (DocumentChange snapshot : value.getDocumentChanges())
                    {
                        Map<String,Object> userName = snapshot.getDocument().getData();

                        String davetliUserName = (String)userName.get("kullaniciAdi");

                        if(davetlilerKullaniciList.contains(davetliUserName)==false)
                        {
                            davetlilerKullaniciList.add(davetliUserName);
                            davetlilerDurumList.add(finalStatus);
                            davetlilerMailList.add(CurrentId);

                            eventDurumlariGoruntulemeAdapterXX.notifyDataSetChanged();
                        }

                    }

                }



            }
        });
    }
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    public void anaSayfayaDon(View view)
    {
        Intent intent = new Intent(EventDurumlariGoruntuleActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void guncelleButton(View view)
    {
        DocumentReference documentReference = firebaseFirestore.collection("Event").document(currentEventId).
                collection("davetliler").document(firebaseAuth.getCurrentUser().getEmail());

        HashMap<String, String> status = new HashMap<>();
        String durum = durumEditText.getText().toString();
        status.put("status",durum);
            documentReference.set(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EventDurumlariGoruntuleActivity.this,"Güncellendi",Toast.LENGTH_LONG).show();
                    durumEditText.setText("");

                }
            });
    }
}