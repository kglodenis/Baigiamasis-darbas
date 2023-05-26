package com.example.baigiamasisdarbas.fxControllers.ui.ads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.Ad;

import java.util.ArrayList;

public class AdRecyclerAdapter extends RecyclerView.Adapter<AdRecyclerAdapter.MyViewHolder> {

    private ArrayList<Ad> adsList;

    public AdRecyclerAdapter(ArrayList<Ad> adsList){
        this.adsList = adsList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView date, company, title, description;
        public MyViewHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.adDateField);
            company = view.findViewById(R.id.adCompany);
            title = view.findViewById(R.id.adTitle);
            description = view.findViewById(R.id.adDescription);
        }
    }
    @NonNull
    @Override
    public AdRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date.setText(adsList.get(position).getDate());
        holder.company.setText(adsList.get(position).getCompanyName());
        holder.title.setText(adsList.get(position).getTitle());
        holder.description.setText(adsList.get(position).getDescription());
    }


    @Override
    public int getItemCount() {
        return adsList.size();
    }


}
