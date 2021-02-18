package com.mustafaeren.planmaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class arkadasGoruntuleRecyclerAdapter extends RecyclerView.Adapter<arkadasGoruntuleRecyclerAdapter.PostHolder> {

    private ArrayList<String> friendKullaniciAdiList;
    private ArrayList<String> friendEmailList;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    public arkadasGoruntuleRecyclerAdapter(ArrayList<String> friendKullaniciAdiList, ArrayList<String> friendEmailList) {
        this.friendKullaniciAdiList = friendKullaniciAdiList;
        this.friendEmailList = friendEmailList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_friends,parent,false);
        return new arkadasGoruntuleRecyclerAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.friendEmailAdapter.setText(friendEmailList.get(position));
        holder.friendKullaniciAdiAdapter.setText(friendKullaniciAdiList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendEmailList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        TextView friendKullaniciAdiAdapter;
        TextView friendEmailAdapter;
        Button arkadasCikar;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            friendEmailAdapter = itemView.findViewById(R.id.friendEmail);
            friendKullaniciAdiAdapter = itemView.findViewById(R.id.friendKullaniciAdi);
            arkadasCikar = itemView.findViewById(R.id.arkadasliktanCikar);
            arkadasCikar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    final View v1 = view;
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Arkadaşlıktan Çıkar");
                    builder.setMessage(friendKullaniciAdiAdapter.getText()+" kullanıcı adlı kişiyi arkadaş listenden çıkarmak istiyor musun ? ");
                    builder.setNegativeButton("Hayır", null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final CollectionReference collectionReference = firebaseFirestore.collection("Friends");
                            final View v2 = v1;
//                            collectionReference.whereEqualTo("hesap",firebaseAuth.getCurrentUser().getEmail()).
//                                    whereEqualTo("arkadaş",friendEmailAdapter.getText()).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    final View v3 = v2;
//                                    if(value!=null)
//                                    {
//                                        for(DocumentSnapshot snapshot : value.getDocuments())
//                                        {
//                                            String id = snapshot.getId();
//
//                                            DocumentReference documentReference = firebaseFirestore.collection("Friends").
//                                                    document(id);
//                                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    final View v4 = v3;
//                                                    Toast.makeText(v4.getContext(),"Arkadaşlıktan Çıkarıldı",Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//
//                                            break;
//                                        }
//
//                                        // BURDAN SONRASI YENI YAZILDI
//                                        CollectionReference collectionReference1 = collectionReference;
//                                        collectionReference1.whereEqualTo("arkadaş",firebaseAuth.getCurrentUser().getEmail()).
//                                                whereEqualTo("hesap",friendEmailAdapter.getText()).
//                                                addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                                        final View v3 = v2;
//                                                        if(value!=null)
//                                                        {
//                                                            for(DocumentSnapshot snapshot : value.getDocuments())
//                                                            {
//                                                                String id = snapshot.getId();
//
//                                                                DocumentReference documentReference = firebaseFirestore.collection("Friends").
//                                                                        document(id);
//                                                                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                                        final View v4 = v3;
//                                                                        Toast.makeText(v4.getContext(),"Arkadaşlıktan Çıkarıldı",Toast.LENGTH_LONG).show();
//                                                                    }
//                                                                });
//
//                                                                break;
//                                                            }
//                                                        }
//                                                    }
//                                                });
//                                    }
//                                }
//                            });


//
//
//                            }

                            collectionReference.whereEqualTo("hesap",firebaseAuth.getCurrentUser().getEmail()).
                                    whereEqualTo("arkadaş",friendEmailAdapter.getText()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    final View v3 = v2;
                                    if(task.isSuccessful()) {
                                        for (DocumentSnapshot snapshot : task.getResult()) {
                                            String id = snapshot.getId();

                                            DocumentReference documentReference = firebaseFirestore.collection("Friends").
                                                    document(id);
                                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    final View v4 = v3;
                                                    Toast.makeText(v4.getContext(), "Arkadaşlıktan Çıkarıldı", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                            break;
                                        }
                                        CollectionReference collectionReference1 = collectionReference;
                                        collectionReference1.whereEqualTo("arkadaş",firebaseAuth.getCurrentUser().getEmail()).
                                                whereEqualTo("hesap",friendEmailAdapter.getText()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                final View v3 = v2;
                                                if(task.isSuccessful()) {
                                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                                        String id = snapshot.getId();

                                                        DocumentReference documentReference = firebaseFirestore.collection("Friends").
                                                                document(id);
                                                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                final View v4 = v3;
                                                                Toast.makeText(v4.getContext(), "Arkadaşlıktan Çıkarıldı", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                        break;
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                            // BURDAN SONRASI YENI YAZILDI
//                            CollectionReference collectionReference1 = collectionReference;
//                            collectionReference1.whereEqualTo("arkadaş",firebaseAuth.getCurrentUser().getEmail()).
//                                    whereEqualTo("hesap",friendEmailAdapter.getText()).
//                                    addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                            final View v3 = v2;
//                                            if(value!=null)
//                                            {
//                                                for(DocumentSnapshot snapshot : value.getDocuments())
//                                                {
//                                                    String id = snapshot.getId();
//
//                                                    DocumentReference documentReference = firebaseFirestore.collection("Friends").
//                                                            document(id);
//                                                    documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            final View v4 = v3;
//                                                            Toast.makeText(v4.getContext(),"Arkadaşlıktan Çıkarıldı",Toast.LENGTH_LONG).show();
//                                                        }
//                                                    });
//
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    });



//                            // BURDAN SONRASI YENI YAZILDI - OLD
//
//                            collectionReference.whereEqualTo("arkadaş",firebaseAuth.getCurrentUser().getEmail()).
//                                    whereEqualTo("hesap",friendEmailAdapter.getText()).
//                                    addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    final View v3 = v2;
//                                    if(value!=null)
//                                    {
//                                        for(DocumentSnapshot snapshot : value.getDocuments())
//                                        {
//                                            String id = snapshot.getId();
//
//                                            DocumentReference documentReference = firebaseFirestore.collection("Friends").
//                                                    document(id);
//                                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    final View v4 = v3;
//                                                    Toast.makeText(v4.getContext(),"Arkadaşlıktan Çıkarıldı",Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//
//                                            break;
//                                        }
//                                    }
//                                }
//                            });

                        }
                    });
                    builder.show();
                }
            });

        }
    }
}
