package com.mustafaeren.planmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class signUpActivity extends AppCompatActivity {

    EditText emailAdresiKayit;
    EditText sifreKayit;
    String kullaniciAdi;
    EditText kullaniciAdiKayit;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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

        emailAdresiKayit = findViewById(R.id.emailAdresiKayit);
        sifreKayit = findViewById(R.id.sifreKayit);
        kullaniciAdiKayit = findViewById(R.id.kullaniciAdiKayit);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void girisYapButton(View view)
    {
        Intent intent = new Intent(signUpActivity.this,signInActivity.class);
        startActivity(intent);

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

    public void kaydolButton(View view)
    {
        String email = emailAdresiKayit.getText().toString();
        kullaniciAdi = kullaniciAdiKayit.getText().toString();
        String sifre = sifreKayit.getText().toString();

        if(email.matches(""))
        {
            Toast.makeText(signUpActivity.this,"Email bos birakilamaz", Toast.LENGTH_LONG).show();
        }
        if(kullaniciAdi.matches(""))
        {
            Toast.makeText(signUpActivity.this,"Kullanici adi bos birakilamaz",Toast.LENGTH_LONG).show();
        }
        if(sifre.matches(""))
        {
            Toast.makeText(signUpActivity.this,"Sifre bos birakilamaz",Toast.LENGTH_LONG).show();
        }

        firebaseAuth.createUserWithEmailAndPassword(email,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                defaultUserProfile();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });



    }

    public void defaultUserProfile()
    {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String  userEmail = user.getEmail();
        String userKullaniciAdi = kullaniciAdi;

        HashMap<String,Object> data = new HashMap<>();
        data.put("userEmail",userEmail);
        data.put("kullaniciAdi",userKullaniciAdi);
        data.put("date", FieldValue.serverTimestamp());

        firebaseFirestore.collection("UserProfile").document(userEmail).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Intent intent =  new Intent(signUpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });



    }
}