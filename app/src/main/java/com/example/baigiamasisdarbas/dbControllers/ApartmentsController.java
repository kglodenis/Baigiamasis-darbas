package com.example.baigiamasisdarbas.dbControllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.baigiamasisdarbas.ds.ApartmentBuilding;
import com.example.baigiamasisdarbas.ds.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ApartmentsController {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public void getAllApartmentsAddresses(final OnGetDataListener<ArrayList<String>> listener) {

        fStore.collection("Apartments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> adresai = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        adresai.add(document.getString("address"));
                        Log.d("Pranesimas", String.valueOf(adresai));

                    }
                    listener.onSuccess(adresai);
                } else {

                    listener.onSuccess(null);
                }
            }
        });
    }

    public void getAllApartments(final OnGetDataListener<ArrayList<ApartmentBuilding>> listener) {
        fStore.collection("Apartments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<ApartmentBuilding> apartments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ApartmentBuilding apartmentBuilding = document.toObject(ApartmentBuilding.class);
                        apartments.add(apartmentBuilding);
                        listener.onSuccess(apartments);
                    }
                } else {

                    listener.onSuccess(null);
                }
            }
        });
    }


    public void createApartment(ApartmentBuilding apartmentBuilding, final OnGetDataListener<String> listener) {
        fStore.collection("Apartments").document(apartmentBuilding.getAddress())
                .set(apartmentBuilding)
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

    public void updateApartment(ApartmentBuilding apartmentBuilding, String id, final OnGetDataListener<String> listener) {
        fStore.collection("Apartments").document(id)
                .update("address", apartmentBuilding.getAddress(), "floors", apartmentBuilding.getFloors(), "responsiblePersonNameAndSurname", apartmentBuilding.getResponsiblePersonNameAndSurname(), "responsiblePersonNumber", apartmentBuilding.getResponsiblePersonNumber(), "cleaningSchedule", apartmentBuilding.getCleaningSchedule())
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

    public void getApartmentByAddress(String address, final OnGetDataListener<ApartmentBuilding> listener) {
        CollectionReference apartmentsRef = fStore.collection("Apartments");
        apartmentsRef.whereEqualTo("address", address).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        ApartmentBuilding apartmentBuilding = doc.toObject(ApartmentBuilding.class);
                        listener.onSuccess(apartmentBuilding);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);

                    }
                });
    }

    public void getUserApartment(final OnGetDataListener<ApartmentBuilding> listener) {

        UserController userController = new UserController();
        userController.getCurrentUser(new OnGetDataListener<User>() {
            @Override
            public void onSuccess(User user) {
                CollectionReference apartmentsRef = fStore.collection("Apartments");
                if (!Objects.equals(user.getUserType(), "ADMIN")) {
                    DocumentReference docRef = apartmentsRef.document(user.getApartmentBuilding());

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ApartmentBuilding apartmentBuilding = document.toObject(ApartmentBuilding.class);
                                    listener.onSuccess(apartmentBuilding);


                                } else {
                                    listener.onFailure(task.getException());
                                    System.out.println("Dokumentas neegzistuoja");
                                }
                            } else {
                                System.out.println("Klaida gaunant dokumentą: " + task.getException());
                            }
                        }
                    });
                }

            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public void deleteApartment(String address, final OnGetDataListener<String> listener) {
        DocumentReference documentRef = fStore.collection("Apartments").document(address);
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
