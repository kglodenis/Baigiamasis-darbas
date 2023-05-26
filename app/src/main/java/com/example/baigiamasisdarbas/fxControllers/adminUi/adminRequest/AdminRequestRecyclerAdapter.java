package com.example.baigiamasisdarbas.fxControllers.adminUi.adminRequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.Request;

import java.util.ArrayList;

public class AdminRequestRecyclerAdapter extends RecyclerView.Adapter<AdminRequestRecyclerAdapter.MyViewHolder> {

    private AdminRecyclerViewClickListener listener;
    private ArrayList<Request> requestList;
    private Context mContext;

    public AdminRequestRecyclerAdapter(Context context, AdminRecyclerViewClickListener listener, ArrayList<Request> requestList) {
        this.mContext = context;
        this.listener = listener;
        this.requestList = requestList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date;
        private TextView description;
        private TextView statusView;
        private ConstraintLayout constraintLayout;


        public MyViewHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.requestDateField);
            description = view.findViewById(R.id.requestDescriptionTextView);
            statusView = view.findViewById(R.id.requestStatusField);
            constraintLayout = view.findViewById(R.id.requestListConstraintLayout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AdminRequestRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRequestRecyclerAdapter.MyViewHolder holder, int position) {
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

    public interface AdminRecyclerViewClickListener {
        void onClick(View v, int position);
    }


}
