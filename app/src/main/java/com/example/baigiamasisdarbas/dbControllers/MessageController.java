package com.example.baigiamasisdarbas.dbControllers;

import androidx.annotation.NonNull;

import com.example.baigiamasisdarbas.ds.Message;
import com.example.baigiamasisdarbas.ds.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MessageController {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    public void getAllMessagesByApartment(final OnGetDataListener<ArrayList<Message>> listener) {
        String userId = currentUser.getUid();
        CollectionReference usersRef = fStore.collection("Users");
        usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        CollectionReference collectionRef = fStore.collection("Messages");
                        collectionRef.whereEqualTo("apartmentBuilding", user.getApartmentBuilding()).orderBy("date", Query.Direction.ASCENDING).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                        ArrayList<Message> messages = new ArrayList<>();
                                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                            //String id = document.getString("msgId");
                                            String senderId = document.getString("senderId");
                                            String messageText = document.getString("message");
                                            String apartment = document.getString("apartmentBuilding");
                                            String date = document.getString("date");
                                            String fullName = document.getString("senderFullName");
                                            Message message = new Message(senderId, fullName, messageText, apartment, date);
                                            messages.add(message);
                                        }
                                        listener.onSuccess(messages);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onFailure(e);
                                    }
                                });

                    } else {
                        listener.onSuccess(null);
                    }
                } else {
                    listener.onFailure(task.getException());
                }
            }
        });


    }


    public void addNewMessage(Message message, final OnGetDataListener<String> listener) {
        fStore.collection("Messages")
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onSuccess("Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }
}

