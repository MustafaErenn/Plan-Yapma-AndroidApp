package com.mustafaeren.planmaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class eventDurumlariGoruntulemeAdapter extends RecyclerView.Adapter<eventDurumlariGoruntulemeAdapter.EventDurumHolder> {

    private ArrayList<String> davetlilerList;
    private ArrayList<String> davetlilerDurum;
    private ArrayList<String> davetlilerKullaniciList;


    public eventDurumlariGoruntulemeAdapter(ArrayList<String> davetlilerList, ArrayList<String> davetlilerDurum, ArrayList<String> davetlilerKullaniciList) {
        this.davetlilerList = davetlilerList;
        this.davetlilerDurum = davetlilerDurum;
        this.davetlilerKullaniciList = davetlilerKullaniciList;
    }

    @NonNull
    @Override
    public EventDurumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_event_durum,parent,false);

        return new eventDurumlariGoruntulemeAdapter.EventDurumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventDurumHolder holder, int position) {
        holder.davetliMail.setText(davetlilerList.get(position));
        holder.davetliDurum.setText(davetlilerDurum.get(position));
        holder.davetliKullaniciAdi.setText(davetlilerKullaniciList.get(position));
    }

    @Override
    public int getItemCount() {
        return davetlilerKullaniciList.size();
    }

    public class EventDurumHolder extends RecyclerView.ViewHolder {
        TextView davetliMail;
        TextView davetliDurum;
        TextView davetliKullaniciAdi;
        public EventDurumHolder(@NonNull View itemView) {
            super(itemView);

            davetliMail = itemView.findViewById(R.id.davetlMail);
            davetliDurum = itemView.findViewById(R.id.davetliDurumu);
            davetliKullaniciAdi = itemView.findViewById(R.id.davetliKullaniciAdi);


        }
    }
}
