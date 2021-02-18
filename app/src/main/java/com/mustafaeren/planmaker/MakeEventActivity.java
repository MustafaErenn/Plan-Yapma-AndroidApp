package com.mustafaeren.planmaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MakeEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Button tarihBelirleButton;
    Button zamanBelirleButton;
    Button davetEtmeButtonu;

    TextView tarihTextView;
    TextView zamanTextView;

    EditText eventNameEditText;
    EditText eventAciklamaEditText;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String currentDateString;
    String currentTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

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

        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventAciklamaEditText = findViewById(R.id.eventAciklamaEditText);

        tarihTextView = findViewById(R.id.eventDateEditText);
        zamanTextView = findViewById(R.id.eventTimeEditText);
        tarihBelirleButton = findViewById(R.id.tarihButonu);

        tarihBelirleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        zamanBelirleButton = findViewById(R.id.saatButonu);
        zamanBelirleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        davetEtmeButtonu = findViewById(R.id.davetButonu);

        davetEtmeButtonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> eventData = new HashMap<>();

                eventData.put("eventAdi",eventNameEditText.getText().toString());
                eventData.put("eventAciklama",eventAciklamaEditText.getText().toString());
                eventData.put("eventSaat",currentTimeString);
                eventData.put("eventTarih",currentDateString);
                eventData.put("date", FieldValue.serverTimestamp());



                firebaseFirestore.collection("Event").document(firebaseAuth.getCurrentUser().getEmail()+" "+eventNameEditText.getText().toString()).
                        set(eventData, SetOptions.merge());


                Intent intent = new Intent(MakeEventActivity.this,arkadaslariDavetEtmeActivity.class);
                intent.putExtra("eventName",eventNameEditText.getText().toString());
                startActivity(intent);



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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,i);
        calendar.set(Calendar.MONTH,i1);
        calendar.set(Calendar.DAY_OF_MONTH,i2);

        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tarihTextView.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        currentTimeString = ("Saat: "+ i + ". " + i1);
        zamanTextView.setText(currentTimeString);

    }
}