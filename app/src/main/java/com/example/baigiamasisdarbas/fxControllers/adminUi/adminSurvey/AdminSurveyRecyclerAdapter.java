package com.example.baigiamasisdarbas.fxControllers.adminUi.adminSurvey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.Survey;

import java.util.ArrayList;

public class AdminSurveyRecyclerAdapter extends RecyclerView.Adapter<AdminSurveyRecyclerAdapter.MyViewHolder> {

    private ArrayList<Survey> surveysList;
    private RecyclerViewClickListener listener;

    public AdminSurveyRecyclerAdapter(RecyclerViewClickListener listener, ArrayList<Survey> surveysList) {
        this.listener = listener;
        this.surveysList = surveysList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date, title, description;

        public MyViewHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.surveyDateField);
            title = view.findViewById(R.id.surveyTitle);
            description = view.findViewById(R.id.surveyDescription);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AdminSurveyRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSurveyRecyclerAdapter.MyViewHolder holder, int position) {
        holder.date.setText(surveysList.get(position).getCreationDate());
        holder.title.setText(surveysList.get(position).getTitle());
        holder.description.setText(surveysList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return surveysList.size();
    }

    public interface RecyclerViewClickListener {

        void onClick(View v, int position);
    }
}
