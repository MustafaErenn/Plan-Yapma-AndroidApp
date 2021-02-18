package com.mustafaeren.planmaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    LinearLayout linear;
    TextView nav_header_name ;
    TextView nav_header_email;


    String istekAtan;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    ArrayList<String> friendRequestList;
    int counter;
    int begin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        friendRequestList = new ArrayList<>();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nav_header_name = (TextView)headerView.findViewById(R.id.nav_headerName);
        nav_header_email = headerView.findViewById(R.id.nav_headerEmail);





        readDataForProfile();
        getFriendRequests();


        NavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);



        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();


        if(savedInstanceState==null)
        {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmen_container,
                    new EventsFragment()).commit();
            nav_view.setCheckedItem(R.id.nav_event);
        }

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmen_container,
                        new FriendsFragment()).commit();
                break;
            case R.id.nav_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmen_container,
                        new EventsFragment()).commit();
                break;
            case R.id.nav_addFriends:
                Intent intentToAddFriend = new Intent(MainActivity.this,AddFriendActivity.class);

                startActivity(intentToAddFriend);
                break;
            case R.id.nav_makeEvent:
                Intent intentToMakeEvent = new Intent(MainActivity.this,MakeEventActivity.class);
                startActivity(intentToMakeEvent);
                break;
            case R.id.nav_out:
                firebaseAuth.signOut();
                Intent intentToSignIn = new Intent(MainActivity.this,signInActivity.class);
                startActivity(intentToSignIn);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void readDataForProfile()
    {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String currentUserEmail = user.getEmail();
        DocumentReference documentReference = firebaseFirestore.collection("UserProfile").document(currentUserEmail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot snapshot = task.getResult();

                    if(snapshot.exists())
                    {

                        Map<String,Object> data = snapshot.getData();
                        String currentUserKullaniciAdi = (String) data.get("kullaniciAdi");

                        nav_header_name.setText(currentUserKullaniciAdi);
                        nav_header_email.setText(firebaseAuth.getCurrentUser().getEmail());


                    }
                }
            }

        });


    }

    public void getFriendRequests()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String currentUserEmail= user.getEmail();
        CollectionReference collectionReference = firebaseFirestore.collection("FriendRequests");
        collectionReference.whereEqualTo("atilan",currentUserEmail).orderBy("date", Query.Direction.DESCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                    if(error != null)
                    {
                        Toast.makeText(MainActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                    }
                    if(value!=null)
                    {

                        friendRequestList.clear();

                        counter = value.size();

                        for (DocumentSnapshot snapShot : value.getDocuments())
                        {

                            Map<String,Object> requestData = snapShot.getData();

                            istekAtan = (String) requestData.get("atan");

                            friendRequestList.add(istekAtan);



                        }

                        istekDegerlendirme(istekAtan);
                    }
            }
        });


    }

    public void istekDegerlendirme(final String istekAtanMail)
    {

        if(counter>0)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Arkadaşlık İsteği");
            builder.setMessage(istekAtanMail + " mailine sahip kullanıcı arkadaşlık isteği geldi ... ");
            builder.setNegativeButton("Reddet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteFriendRequest(istekAtanMail);
                }
            });
            builder.setPositiveButton("Kabul Et", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Friends(istekAtanMail);
                }
            });
            builder.show();
            counter--;
        }


    }
    public void Friends(final String istekAtanMail)
    {
        HashMap<String,Object> friendData = new HashMap<>();
        friendData.put("hesap",firebaseAuth.getCurrentUser().getEmail());
        friendData.put("arkadaş",istekAtanMail);
        friendData.put("date", FieldValue.serverTimestamp());

        HashMap<String,Object> friendData2 = new HashMap<>();
        friendData2.put("hesap",istekAtanMail);
        friendData2.put("arkadaş",firebaseAuth.getCurrentUser().getEmail());
        friendData2.put("date", FieldValue.serverTimestamp());

        firebaseFirestore.collection("Friends").add(friendData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                deleteFriendRequest(istekAtanMail);
                Toast.makeText(MainActivity.this,"Arkadaş Eklendi",Toast.LENGTH_LONG)
                        .show();


            }
        });

        firebaseFirestore.collection("Friends").add(friendData2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        });



    }
    public void deleteFriendRequest(String istekAtanMail)
    {
        // ID'SI BILINEN DOCUMENTLERI SILIYOR.
//        DocumentReference documentReference = firebaseFirestore.collection("FriendRequests").
//                document("BUWmrJRG8LR5vV2uUUtS");
//
//        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(MainActivity.this,"SILINDI",Toast.LENGTH_LONG).show();
//            }
//        });

        CollectionReference collectionReference = firebaseFirestore.collection("FriendRequests");
        collectionReference.whereEqualTo("atilan",firebaseAuth.getCurrentUser().getEmail()).whereEqualTo("atan",istekAtanMail).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                for (DocumentSnapshot snapShot : value.getDocuments())
                {
                    String id = snapShot.getId();


                    deleteFriendRequestsFromFB(id);
                    break;
                }
            }
        });


    }

    public void deleteFriendRequestsFromFB(String id)
    {

                DocumentReference documentReference = firebaseFirestore.collection("FriendRequests").
                document(id);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

}