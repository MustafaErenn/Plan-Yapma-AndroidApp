package com.mustafaeren.planmaker;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class arkadasEkleRecyclerAdapter extends RecyclerView.Adapter<arkadasEkleRecyclerAdapter.PostHolder> {

    private ArrayList<String> userKullaniciAdi;
    private ArrayList<String> userEmail;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    public arkadasEkleRecyclerAdapter(ArrayList<String> userKullaniciAdi, ArrayList<String> userEmail) {
        this.userKullaniciAdi = userKullaniciAdi;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public arkadasEkleRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new arkadasEkleRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull arkadasEkleRecyclerAdapter.PostHolder holder, int position) {
        holder.kullaniciEmailAdapter.setText(userEmail.get(position));
        holder.kullaniciAdiAdapter.setText(userKullaniciAdi.get(position));
    }

    @Override
    public int getItemCount() {
        return userKullaniciAdi.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView kullaniciAdiAdapter;
        TextView kullaniciEmailAdapter;
        TextView button;
        TextView nav_header_NameAdapter;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            kullaniciAdiAdapter = itemView.findViewById(R.id.arananKullaniciAdi);
            kullaniciEmailAdapter = itemView.findViewById(R.id.arananEmail);


            button = itemView.findViewById(R.id.arkadasEkle);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {


                    HashMap<String,Object> requestData = new HashMap<>();

                    requestData.put("atan",user.getEmail());
                    requestData.put("atilan",kullaniciEmailAdapter.getText().toString());
                    requestData.put("date", FieldValue.serverTimestamp());



                    firebaseFirestore.collection("FriendRequests").add(requestData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(view.getContext(),"Arkadaşlık isteği gönderildi",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(view.getContext(),MainActivity.class);
                            view.getContext().startActivity(intent);
                        }
                    });
               }
            });

        }
    }
}
