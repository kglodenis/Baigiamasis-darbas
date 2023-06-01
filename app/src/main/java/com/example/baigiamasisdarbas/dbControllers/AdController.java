package com.example.baigiamasisdarbas.dbControllers;

import androidx.annotation.NonNull;

import com.example.baigiamasisdarbas.ds.Ad;
import com.example.baigiamasisdarbas.ds.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdController {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = fAuth.getCurrentUser();

    public void getAllAds(final OnGetDataListener<ArrayList<Ad>> listener) {
        UserController userController = new UserController();
        userController.getCurrentUser(new OnGetDataListener<User>() {
            @Override
            public void onSuccess(User data) {
                fStore.collection("Ads").whereEqualTo("apartment", data.getApartmentBuilding()).orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Ad> ads = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Ad ad = document.toObject(Ad.class);
                                ads.add(ad);
                                listener.onSuccess(ads);
                            }
                        } else {

                            listener.onSuccess(null);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    public void createAd(Ad ad, final OnGetDataListener<String> listener) {
        fStore.collection("Ads").add(ad).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentId = documentReference.getId();
                DocumentReference apartment = fStore.collection("Ads").document(documentId);
                apartment.update("id", documentId);
                listener.onSuccess("Success");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public void deleteAd(String id, final OnGetDataListener<String> listener) {
        DocumentReference documentRef = fStore.collection("Ads").document(id);
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

    public void updateAd(Ad ad, final OnGetDataListener<String> listener) {

        fStore.collection("Ads").document(ad.getId())
                .update("title", ad.getTitle(), "description", ad.getDescription(), "modificationDate", ad.getModificationDate(), "apartment", ad.getApartment())

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
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
