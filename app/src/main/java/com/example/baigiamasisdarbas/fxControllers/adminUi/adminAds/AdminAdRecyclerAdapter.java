package com.example.baigiamasisdarbas.fxControllers.adminUi.adminAds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.Ad;

import java.util.ArrayList;

public class AdminAdRecyclerAdapter extends RecyclerView.Adapter<AdminAdRecyclerAdapter.MyViewHolder> {
    private RecyclerViewClickListener listener;
    private ArrayList<Ad> adsList;

    public AdminAdRecyclerAdapter(RecyclerViewClickListener listener, ArrayList<Ad> adsList) {
        this.listener = listener;
        this.adsList = adsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date, company, title, description;

        public MyViewHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.adDateField);
            company = view.findViewById(R.id.adCompany);
            title = view.findViewById(R.id.adTitle);
            description = view.findViewById(R.id.adDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());


        }
    }

    @NonNull
    @Override
    public AdminAdRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false); //(R.layout.request_items, parent)nera false
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdRecyclerAdapter.MyViewHolder holder, int position) {
        holder.date.setText(adsList.get(position).getDate());
        holder.company.setText(adsList.get(position).getCompanyName());
        holder.title.setText(adsList.get(position).getTitle());
        holder.description.setText(adsList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }


}
