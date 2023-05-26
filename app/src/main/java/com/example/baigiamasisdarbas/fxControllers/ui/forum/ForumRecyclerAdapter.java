package com.example.baigiamasisdarbas.fxControllers.ui.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.ds.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.MyViewHolder2> {

    private Context context;
    private ArrayList<Message> requestList;

    public ForumRecyclerAdapter(ArrayList<Message> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;
        private TextView oppoName, myName;

        public MyViewHolder2(final View view) {
            super(view);
            oppoLayout = view.findViewById(R.id.oppoLayout);
            myLayout = view.findViewById(R.id.myLayout);
            oppoMessage = view.findViewById(R.id.oppoMessage);
            myMessage = view.findViewById(R.id.myMessage);
            oppoTime = view.findViewById(R.id.oppoMsgTime);
            myTime = view.findViewById(R.id.myMsgTime);
            oppoName = view.findViewById(R.id.oppoNameAndSurname);
            myName = view.findViewById(R.id.myNameAndSurname);
        }

    }

    @NonNull
    @Override
    public ForumRecyclerAdapter.MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_message, null);
        return new ForumRecyclerAdapter.MyViewHolder2(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumRecyclerAdapter.MyViewHolder2 holder, int position) {

        String text = requestList.get(position).getMessage();
        String sender = requestList.get(position).getSenderFullName();
        String date = requestList.get(position).getDate();

        LinearLayout main;
        if (requestList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(text);
            holder.myTime.setText(date);
            holder.myName.setText(sender);

        } else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(text);
            holder.oppoTime.setText(date);
            holder.oppoName.setText(sender);
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }


}
