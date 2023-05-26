package com.example.baigiamasisdarbas.fxControllers.adminUi.adminApartments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.ApartmentBuilding;

import java.util.ArrayList;

public class AdminApartmentRecyclerAdapter extends RecyclerView.Adapter<AdminApartmentRecyclerAdapter.MyViewHolder> {

    private AdminApartmentRecyclerViewClickListener listener;
    private ArrayList<ApartmentBuilding> apartmentsList;

    public AdminApartmentRecyclerAdapter(AdminApartmentRecyclerViewClickListener listener, ArrayList<ApartmentBuilding> apartmentsList) {
        this.listener = listener;
        this.apartmentsList = apartmentsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTxt;

        public MyViewHolder(final View view) {
            super(view);
            nameTxt = view.findViewById(R.id.requestDescriptionTextView);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AdminApartmentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartment_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminApartmentRecyclerAdapter.MyViewHolder holder, int position) {
        String address = apartmentsList.get(position).getAddress();
        holder.nameTxt.setText(address);
    }

    @Override
    public int getItemCount() {
        return apartmentsList.size();
    }

    public interface AdminApartmentRecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
