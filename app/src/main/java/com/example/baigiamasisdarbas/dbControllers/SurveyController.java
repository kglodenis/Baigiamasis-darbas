package com.example.baigiamasisdarbas.dbControllers;

import androidx.annotation.NonNull;

import com.example.baigiamasisdarbas.ds.Survey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SurveyController {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public void createSurvey(Survey survey, final OnGetDataListener<String> listener) {
        fStore.collection("Surveys").add(survey).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentId = documentReference.getId();
                DocumentReference surveyRef = fStore.collection("Surveys").document(documentId);
                surveyRef.update("id", documentId);
                listener.onSuccess("Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public void getAllSurveys(final OnGetDataListener<ArrayList<Survey>> listener) {
        fStore.collection("Surveys").orderBy("creationDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Survey> surveys = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Survey survey = document.toObject(Survey.class);
                                surveys.add(survey);
                                listener.onSuccess(surveys);
                            }
                        } else {
                            listener.onSuccess(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public void updateSurvey(Survey survey, final OnGetDataListener<String> listener) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String rightNow = currentDate + " " + currentTime;
        fStore.collection("Surveys").document(survey.getId())
                .update("title", survey.getTitle(), "description", survey.getDescription(), "url", survey.getUrl(), "apartment", survey.getApartment())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        fStore.collection("Surveys").document(survey.getId()).update("modificationDate", rightNow);
                        listener.onSuccess("Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public void deleteSurvey(String id, final OnGetDataListener<String> listener) {
        DocumentReference documentRef = fStore.collection("Surveys").document(id);
        documentRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess("Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });

    }


}
