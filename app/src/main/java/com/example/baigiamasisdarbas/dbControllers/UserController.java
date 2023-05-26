package com.example.baigiamasisdarbas.dbControllers;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.baigiamasisdarbas.ds.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserController {


    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ;
    FirebaseStorage fStorage = FirebaseStorage.getInstance();

    FirebaseUser currentUser = fAuth.getCurrentUser();


    public void registerUser(String email, String password, String name, String surname, String phoneNumber, String appartmentBuilding) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String date = currentDate + " " + currentTime;
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("Users").document(userID);
                    User user = new User(userID, name, surname, email, phoneNumber, appartmentBuilding, date);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("pranesimas", "User was added successfully + " + userID);
                                    DocumentReference apartment = fStore.collection("Apartments").document(appartmentBuilding);
                                    apartment.update("allResidents", FieldValue.arrayUnion(userID));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("klaida", e.toString());
                                }
                            });
                } else {
                    Log.d("klaida", "Klaida sukuriant vartotoją");
                }

            }
        });


    }

    public void getCurrentUser(final OnGetDataListener<User> listener) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference usersRef = fStore.collection("Users");
            usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            listener.onSuccess(user);
                        } else {
                            listener.onSuccess(null);
                        }
                    } else {
                        listener.onFailure(task.getException());
                    }
                }
            });
        } else {
            listener.onSuccess(null);
        }
    }

    public void updateEmail(String email) {
        FirebaseUser user = fAuth.getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });

    }

    public void updateUser(User user, final OnGetDataListener<String> listener) {

        updateEmail(user.getEmail());
        fStore.collection("Users").document(currentUser.getUid())
                .update("email", user.getEmail(), "name", user.getName(), "phoneNumber", user.getPhoneNumber(), "surname", user.getSurname())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


    }

    public void updateUser(Uri uri, User user, final OnGetDataListener<String> listener) {
        updateEmail(user.getEmail());
        final StorageReference reference = fStorage.getReference()
                .child("ProfilePictures")
                .child(System.currentTimeMillis() + "");
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        fStore.collection("Users").document(currentUser.getUid())
                                .update("name", user.getName(), "phoneNumber", user.getPhoneNumber(), "surname", user.getSurname(), "pictureUri", uri)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        listener.onSuccess(uri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
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

    public void checkIfEmailExist(String email, final OnGetDataListener<String> listener) {
        CollectionReference usersCollection = fStore.collection("Users");
        Query query = usersCollection.whereEqualTo("email", email);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.isEmpty()) {
                    listener.onSuccess("false");
                } else {
                    listener.onSuccess("true");
                }
            } else {
                System.out.println(task.getException());
            }
        });
    }

    public void deleteUser() {
        fStore.collection("Users").document(currentUser.getUid()).delete();
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }
}


