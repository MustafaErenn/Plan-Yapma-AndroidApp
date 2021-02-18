package com.mustafaeren.planmaker;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class arkadasDavetRecyclerAdapter extends RecyclerView.Adapter<arkadasDavetRecyclerAdapter.PostHolder> {

    private ArrayList<String> davetFriendKullaniciAdiList;
    private ArrayList<String> davetFriendEmailList;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private String eventName;
    private ArrayList<String> davetEdilenListe = new ArrayList<>();

    public arkadasDavetRecyclerAdapter(ArrayList<String> davetFriendKullaniciAdiList, ArrayList<String> davetFriendEmailList, String eventName) {
        this.davetFriendKullaniciAdiList = davetFriendKullaniciAdiList;
        this.davetFriendEmailList = davetFriendEmailList;
        this.eventName = eventName;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_event_friends,parent,false);
        return new arkadasDavetRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.davetFriendEmailAdapter.setText(davetFriendEmailList.get(position));
        holder.davetFriendKullaniciAdiAdapter.setText(davetFriendKullaniciAdiList.get(position));
    }

    @Override
    public int getItemCount() {
        return davetFriendEmailList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        TextView davetFriendKullaniciAdiAdapter;
        TextView davetFriendEmailAdapter;
        Button davetEt;
        TextView eventNameEditText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            davetFriendEmailAdapter = itemView.findViewById(R.id.eventFriendEmail);
            davetFriendKullaniciAdiAdapter = itemView.findViewById(R.id.eventFriendKullaniciAdi);
            davetEt = itemView.findViewById(R.id.eventDavetButonu);



            davetEt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View v1 = view;
                    HashMap<String,Object> eventData = new HashMap<>();
                    eventData.put("status","bilinmiyor");

                    firebaseFirestore.collection("Event").document(firebaseAuth.getCurrentUser().getEmail()+" "+eventName   ).
                            collection("davetliler").document(davetFriendEmailAdapter.getText().toString()).
                            set(eventData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final View v2 = v1;
                            Toast.makeText(v2.getContext(),"Davet Yollandı",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

        }
    }
}
