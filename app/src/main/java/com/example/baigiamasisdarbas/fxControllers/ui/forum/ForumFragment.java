package com.example.baigiamasisdarbas.fxControllers.ui.forum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.MessageController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.UserController;
import com.example.baigiamasisdarbas.ds.Message;
import com.example.baigiamasisdarbas.ds.User;
import com.example.baigiamasisdarbas.fxControllers.ui.request.RequestRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ForumFragment extends Fragment {
    private ArrayList<Message> requestsList;
    private RecyclerView recyclerView;

    private RequestRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        requestsList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
        ImageButton sendButton = view.findViewById(R.id.messageSendButtonForum);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageField = view.findViewById(R.id.messageTextField);
                String messageText = messageField.getText().toString();
                sendMessage(messageText);
                fragmentRefresh();
                messageField.onEditorAction(EditorInfo.IME_ACTION_DONE);
                messageField.setText("");
            }
        });
        return view;
    }

    public void fragmentRefresh() {
        setAdapter();
    }


    private void sendMessage(String messageText) {
        UserController userController = new UserController();
        if (validation(messageText)) {
            userController.getCurrentUser(new OnGetDataListener<User>() {
                @Override
                public void onSuccess(User data) {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String date = currentDate + " " + currentTime;
                    String fullName = data.getName() + " " + data.getSurname();
                    Message message = new Message(FirebaseAuth.getInstance().getUid(), fullName, messageText, data.getApartmentBuilding(), date);
                    MessageController messageController = new MessageController();
                    messageController.addNewMessage(message, new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {

                        }
                        @Override
                        public void onFailure(Exception e) {
                            showMessage("Įvyko klaida \n Klaida: " + e);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("Klaida", "Klaidos pranešimas: " + e);
                }
            });
        }

    }


    private void setAdapter() {
        MessageController messageController = new MessageController();
        messageController.getAllMessagesByApartment(new OnGetDataListener<ArrayList<Message>>() {
            @Override
            public void onSuccess(ArrayList<Message> data) {

                ForumRecyclerAdapter adapter = new ForumRecyclerAdapter(data);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if (recyclerView.getAdapter().getItemCount() - 1 > 0) {
                    int position = recyclerView.getAdapter().getItemCount() - 1;
                    recyclerView.scrollToPosition(position);
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Įvyko klaida \n Klaida: " + e);
            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validation(String message) {

        if (TextUtils.isEmpty(message)) {
            return false;
        }


        return true;
    }

}