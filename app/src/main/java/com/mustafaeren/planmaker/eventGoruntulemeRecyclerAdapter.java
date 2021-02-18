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

import java.util.ArrayList;

public class eventGoruntulemeRecyclerAdapter extends RecyclerView.Adapter<eventGoruntulemeRecyclerAdapter.EventHolder> {

    private ArrayList<String> eventNameList;
    private ArrayList<String> eventAciklamaList;
    private ArrayList<String> eventTarihList;
    private ArrayList<String> eventSaatList;
    private ArrayList<String> eventIDList;

    public eventGoruntulemeRecyclerAdapter(ArrayList<String> eventNameList, ArrayList<String> eventAciklamaList, ArrayList<String> eventTarihList, ArrayList<String> eventSaatList, ArrayList<String> eventIDList) {
        this.eventNameList = eventNameList;
        this.eventAciklamaList = eventAciklamaList;
        this.eventTarihList = eventTarihList;
        this.eventSaatList = eventSaatList;
        this.eventIDList = eventIDList;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_events,parent,false);
        return new eventGoruntulemeRecyclerAdapter.EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.eventAdi.setText(eventNameList.get(position));
        holder.eventAciklama.setText(eventAciklamaList.get(position));
        holder.eventTarih.setText(eventTarihList.get(position));
        holder.eventSaat.setText(eventSaatList.get(position));
        holder.eventId.setText(eventIDList.get(position));

    }

    @Override
    public int getItemCount() {
        return eventNameList.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        TextView eventAdi;
        TextView eventAciklama;
        TextView eventTarih;
        TextView eventSaat;
        TextView eventId;
        Button durumButonu;
        public EventHolder(@NonNull View itemView) {
            super(itemView);

            eventAdi = itemView.findViewById(R.id.eventNameText);
            eventAciklama = itemView.findViewById(R.id.aciklamaText);
            eventTarih = itemView.findViewById(R.id.dateText);
            eventSaat = itemView.findViewById(R.id.timeText);
            eventId = itemView.findViewById(R.id.IdText);
            durumButonu = itemView.findViewById(R.id.button);

            durumButonu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final View v1 = view;
                    // BURADAN DURUMLARI GORUNTULEDIGIN YERE GIDICEKSIN INTENT YAPARKEN EVENTID'YI VERMEYI UNUTMA
                    Intent intent = new Intent(v1.getContext(),EventDurumlariGoruntuleActivity.class);
                    intent.putExtra("eventId",eventId.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v1.getContext().startActivity(intent);



                }
            });
        }
    }
}
