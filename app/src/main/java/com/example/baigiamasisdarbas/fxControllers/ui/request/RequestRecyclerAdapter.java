package com.example.baigiamasisdarbas.fxControllers.ui.request;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.Request;

import java.util.ArrayList;

public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.MyViewHolder> {

    private RecyclerViewClickListener listener;
    private ArrayList<Request> requestList;

    public RequestRecyclerAdapter(ArrayList<Request> requestList, RecyclerViewClickListener listener) {

        this.requestList = requestList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date;
        private TextView description;
        private TextView statusView;

        public MyViewHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.requestDateField);
            description = view.findViewById(R.id.requestDescriptionTextView);
            statusView = view.findViewById(R.id.requestStatusField);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());

        }
    }

    @NonNull
    @Override
    public RequestRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_items, parent, false); //(R.layout.request_items, parent)nera false
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestRecyclerAdapter.MyViewHolder holder, int position) {
        String date = requestList.get(position).getRegistrationDate();
        String description = requestList.get(position).getRequestDescription();
        String status = requestList.get(position).getRequestStatus();

        if (status != null && status.equals("Užregistruotas")) {
            holder.statusView.setText("Užklausa užregistruota");

        } else if (status != null && status.equals("Atšauktas")) {
            holder.statusView.setText("Užklausa atšaukta");


        } else {
            holder.statusView.setText("Užklausa išspręsta");

        }
        holder.date.setText(date);
        holder.description.setText(description);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }


    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
