package com.example.baigiamasisdarbas.dbControllers;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.baigiamasisdarbas.ds.Request;
import com.example.baigiamasisdarbas.ds.User;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequestController {
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ;
    String userID;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseStorage fStorage = FirebaseStorage.getInstance();

    FirebaseUser currentUser = fAuth.getCurrentUser();

    public void createRequest(Request request, final OnGetDataListener<String> listener) {
        fStore.collection("Requests").add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                UserController userController = new UserController();
                String documentId = documentReference.getId();
                userController.getCurrentUser(new OnGetDataListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        // do something with the user object
                        String appartments = user.getApartmentBuilding();
                        String fullName = user.getName() + " " + user.getSurname();
                        DocumentReference request = fStore.collection("Requests").document(documentReference.getId());
                        request.update("apartmentBuilding", appartments);
                        request.update("senderFullName", fullName);
                        request.update("requestId", documentId);
                        DocumentReference apartment = fStore.collection("Apartments").document(appartments);
                        apartment.update("allRequests", FieldValue.arrayUnion(documentReference.getId()));
                        listener.onSuccess(documentId);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        listener.onFailure(e);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public void createRequest(Uri uri, Request request, final OnGetDataListener<String> listener) {
        final StorageReference reference = fStorage.getReference()
                .child("RequestImages")
                .child(System.currentTimeMillis() + "");
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        request.setImage(String.valueOf(uri));
                        fStore.collection("Requests").add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                UserController userController = new UserController();
                                String documentId = documentReference.getId();
                                userController.getCurrentUser(new OnGetDataListener<User>() {
                                    @Override
                                    public void onSuccess(User user) {
                                        // do something with the user object
                                        String appartments = user.getApartmentBuilding();
                                        String fullName = user.getName() + " " + user.getSurname();
                                        DocumentReference request = fStore.collection("Requests").document(documentReference.getId());
                                        request.update("apartmentBuilding", appartments);
                                        request.update("senderFullName", fullName);
                                        request.update("requestId", documentId);
                                        DocumentReference apartment = fStore.collection("Apartments").document(appartments);
                                        apartment.update("allRequests", FieldValue.arrayUnion(documentReference.getId()));
                                        Intent intent = new Intent();
                                        listener.onSuccess(documentId);

                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        listener.onFailure(e);
                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.onFailure(e);
                            }
                        });
                    }
                });
            }
        });
    }

    public void getAllRequestsByUser(final OnGetDataListener<ArrayList<Request>> listener) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference requestsRef = db.collection("Requests");
            requestsRef.whereEqualTo("senderID", userId).orderBy("registrationDate", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            ArrayList<Request> requests = new ArrayList<>();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String requestID = document.getString("requestId");
                                String id = document.getString("senderID");
                                String description = document.getString("requestDescription");
                                String status = document.getString("requestStatus");
                                String date = document.getString("registrationDate");
                                String apartment = document.getString("apartmentBuilding");
                                String phoneNumber = document.getString("phoneNumber");
                                String name = document.getString("senderFullName");
                                String modificationDate = document.getString("modificationDate");

                                Request request = new Request(requestID, id, description, status, date, apartment, phoneNumber, name, modificationDate);
                                requests.add(request);
                            }
                            listener.onSuccess(requests);
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

    public void getRequestById(String requestId, final OnGetDataListener<Request> listener) {
        CollectionReference requestsRef = fStore.collection("Requests");
        requestsRef.whereEqualTo("requestId", requestId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        Request request = new Request();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String requestID = document.getString("requestId");
                            String id = document.getString("senderID");
                            String description = document.getString("requestDescription");
                            String status = document.getString("requestStatus");
                            String date = document.getString("registrationDate");
                            String apartment = document.getString("apartmentBuilding");
                            String phoneNumber = document.getString("phoneNumber");
                            String name = document.getString("senderFullName");
                            String modificationDate = document.getString("modificationDate");

                            request = new Request(requestID, id, description, status, date, apartment, phoneNumber, name, modificationDate);
                        }
                        listener.onSuccess(request);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public void getAllRequests(final OnGetDataListener<ArrayList<Request>> listener) {
        CollectionReference requestsRef = fStore.collection("Requests");
        requestsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        ArrayList<Request> requests = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String requestId = document.getString("requestId");
                            String senderId = document.getString("senderID");
                            String description = document.getString("requestDescription");
                            String status = document.getString("requestStatus");
                            String date = document.getString("registrationDate");
                            String fullName = document.getString("senderFullName");
                            String address = document.getString("apartmentBuilding");
                            String imageUrl = document.getString("image");
                            Request request = new Request(requestId, senderId, fullName, address, description, status, date, imageUrl);
                            requests.add(request);
                        }
                        listener.onSuccess(requests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public void getRequestsByParameters(List<String> requestStatus, List<String> apartments, final OnGetDataListener<ArrayList<Request>> listener) {
        CollectionReference requestsRef = fStore.collection("Requests");
        Query query = requestsRef.whereIn("requestStatus", requestStatus).whereIn("apartmentBuilding", apartments);
        query.orderBy("registrationDate", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        ArrayList<Request> requests = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String requestId = document.getString("requestId");
                            String description = document.getString("requestDescription");
                            String status = document.getString("requestStatus");
                            String date = document.getString("registrationDate");
                            String fullName = document.getString("senderFullName");
                            String address = document.getString("apartmentBuilding");
                            String imageUrl = document.getString("image");
                            String phoneNumber = document.getString("phoneNumber");
                            Request request = new Request(requestId, fullName, address, description, status, date, imageUrl, phoneNumber);
                            requests.add(request);
                        }
                        listener.onSuccess(requests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure(e);
                    }
                });
    }

    public void updateRequestStatusById(String documentId, String status, final OnGetDataListener<String> listener) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String rightNow = currentDate + " " + currentTime;
        DocumentReference requestRef = fStore.collection("Requests").document(documentId);
        requestRef
                .update("requestStatus", status, "modificationDate", rightNow)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public void deleteAllRequests() {
        CollectionReference collectionRef = fStore.collection("Requests");
        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        // Iterate through all the documents and delete them
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            documentSnapshot.getReference().delete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting documents", e);
                    }
                });
    }

    public void getRequestsCountByType(final OnGetDataListener<ArrayList<BarEntry>> listener) {
        ArrayList<BarEntry> counts = new ArrayList<>();
        CollectionReference requestsRef = fStore.collection("Requests");
        Query waterQuery = requestsRef.whereEqualTo("requestType", "Vanduo");
        Query electricityQuery = requestsRef.whereEqualTo("requestType", "Elektra");
        Query cleannessQuery = requestsRef.whereEqualTo("requestType", "Švara");
        Query heatQuery = requestsRef.whereEqualTo("requestType", "Šildymas");
        Query loudnessQuery = requestsRef.whereEqualTo("requestType", "Triukšmas");
        Query otherQuery = requestsRef.whereEqualTo("requestType", "Kita");
        waterQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                counts.add(new BarEntry(1, queryDocumentSnapshots.size()));
                listener.onSuccess(counts);
            }
        });

        electricityQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                counts.add(new BarEntry(2, queryDocumentSnapshots.size()));
                listener.onSuccess(counts);
            }
        });
        cleannessQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                counts.add(new BarEntry(3, queryDocumentSnapshots.size()));
                listener.onSuccess(counts);
            }
        });
        heatQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                counts.add(new BarEntry(4, queryDocumentSnapshots.size()));
                listener.onSuccess(counts);
            }
        });
        loudnessQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                counts.add(new BarEntry(5, queryDocumentSnapshots.size()));
                listener.onSuccess(counts);
            }
        });
        otherQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                counts.add(new BarEntry(6, queryDocumentSnapshots.size()));
                listener.onSuccess(counts);
            }
        });
    }
}
