package com.mustafaeren.planmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AddFriendActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    EditText aramaEditText;

    ArrayList<String> arananElemanKullaniciAdi;
    ArrayList<String> arananElemanMail;
    arkadasEkleRecyclerAdapter arkadasEkleRecyclerAdapterXX;
    Button arkadasEklemeButonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        aramaEditText = findViewById(R.id.aramaKullaniciAdiEditText);

        arkadasEklemeButonu = findViewById(R.id.arkadasEkle);

        arananElemanKullaniciAdi = new ArrayList<>();
        arananElemanMail =  new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewForSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arkadasEkleRecyclerAdapterXX = new arkadasEkleRecyclerAdapter(arananElemanKullaniciAdi,arananElemanMail);
        recyclerView.setAdapter(arkadasEkleRecyclerAdapterXX);


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

    public void searchButton(View view)
    {
        String arananKullaniciAdi = aramaEditText.getText().toString();
        getDataForSearching(arananKullaniciAdi);
    }
    public void arkadasEkleFonksiyonu(View view)
    {
        //System.out.println("ARKADAS EKLEME BUTONU BASILDI");
    }
    public void getDataForSearching(String arananKullaniciAdiParam)
    {
        CollectionReference collectionReference = firebaseFirestore.collection("UserProfile");

        collectionReference.whereEqualTo("kullaniciAdi",arananKullaniciAdiParam).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        arananElemanKullaniciAdi.clear();
                        arananElemanMail.clear();

                        for(DocumentSnapshot snapshot : task.getResult())
                        {

                            Map<String,Object> data  = snapshot.getData();
                            String kullaniciAdi = (String) data.get("kullaniciAdi");
                            String email = (String) data.get("userEmail");

                            arananElemanMail.add(email);
                            arananElemanKullaniciAdi.add(kullaniciAdi);

                            arkadasEkleRecyclerAdapterXX.notifyDataSetChanged();

                        }
                    }
            }
        });



    }
}