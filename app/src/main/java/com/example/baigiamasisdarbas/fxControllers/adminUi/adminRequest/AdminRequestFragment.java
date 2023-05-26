package com.example.baigiamasisdarbas.fxControllers.adminUi.adminRequest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.RequestController;
import com.example.baigiamasisdarbas.ds.Request;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class AdminRequestFragment extends Fragment {

    private ArrayList<Request> requestsList;
    private RecyclerView recyclerView;
    private AdminRequestRecyclerAdapter.AdminRecyclerViewClickListener listener;

    private MaterialCardView selectStatusCard;
    private MaterialCardView selectApartmentCard;
    TextView statusSelection;
    TextView apartmentSelection;
    boolean[] selectedStatuses;
    boolean[] selectedApartments;
    ArrayList<Integer> statusList = new ArrayList<>();
    ArrayList<Integer> apartmentList = new ArrayList<>();
    List<String> requestStatus = new ArrayList<String>();
    List<String> apartments = new ArrayList<String>();
    List<String> apartmentsBackup = new ArrayList<String>();

    String[] statusArray = {"Išspręstas", "Atšauktas", "Užregistruotas"};
    String[] apartmentArray;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_request, container, false);
        recyclerView = view.findViewById(R.id.adminRecyclerView);
        requestsList = new ArrayList<>();
        selectStatusCard = view.findViewById(R.id.selectedCardStatus);
        statusSelection = view.findViewById(R.id.statusSelection);
        selectedStatuses = new boolean[statusArray.length];
        requestStatus.add("Išspręstas");
        requestStatus.add("Atšauktas");
        requestStatus.add("Užregistruotas");
        selectApartmentCard = view.findViewById(R.id.selectedCardApartment);
        apartmentSelection = view.findViewById(R.id.apartmentSelection);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading");
        selectStatusCard.setOnClickListener(v -> {
            showStatusDialog();
        });
        ApartmentsController apartmentsController = new ApartmentsController();
        apartmentsController.getAllApartmentsAddresses(new OnGetDataListener<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                apartments = new ArrayList<>(data);
                apartmentsBackup = new ArrayList<>(data);
                apartmentArray = data.toArray(new String[0]);
                selectedApartments = new boolean[apartmentArray.length];
                selectApartmentCard.setOnClickListener(v -> {
                    showApartmentDialog(selectedApartments, apartmentArray);
                });
                setAdapter(requestStatus, apartments);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);
            }
        });

        return view;

    }


    private void showApartmentDialog(boolean[] selectedApartments, String[] apartmentArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Daugiabučių pasirinkimas");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(apartmentArray, selectedApartments, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    apartmentList.add(which);
                } else {
                    apartmentList.remove(Integer.valueOf(which));
                }

                if (apartmentList.size() == 0) {
                    apartments.clear();
                    apartments.addAll(apartmentsBackup);
                    apartmentSelection.setText("");
                    apartmentSelection.setHint("Pasirinkite daugiabutį");
                }

            }
        }).setPositiveButton("Gerai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder stringBuilder = new StringBuilder();
                if (apartmentList.size() > 0) {
                    apartments.clear();
                    for (int i = 0; i < apartmentList.size(); i++) {
                        stringBuilder.append(apartmentArray[apartmentList.get(i)]);
                        apartments.add(apartmentArray[apartmentList.get(i)]);
                        if (i != apartmentList.size() - 1) {
                            stringBuilder.append(", ");
                        }
                        apartmentSelection.setText(stringBuilder.toString());
                    }
                    setAdapter(requestStatus, apartments);
                } else {
                    setAdapter(requestStatus, apartments);
                }

            }
        }).setNegativeButton("Atšaukti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Išvalyti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedApartments.length; i++) {
                    selectedApartments[i] = false;
                    apartmentList.clear();
                    apartmentSelection.setText("");
                }
                apartments = apartmentsBackup;
                setAdapter(requestStatus, apartments);
            }
        });
        builder.show();
    }

    private void showStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Būsenos pasirinkimas");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(statusArray, selectedStatuses, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    statusList.add(which);
                } else {
                    statusList.remove(Integer.valueOf(which));
                }

                if (statusList.size() == 0) {
                    requestStatus.clear();
                    requestStatus.add("Išspręstas");
                    requestStatus.add("Atšauktas");
                    requestStatus.add("Užregistruotas");
                    statusSelection.setHint("Pasirinkite būseną");
                }

            }
        }).setPositiveButton("Gerai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuilder stringBuilder = new StringBuilder();
                if (statusList.size() > 0) {
                    requestStatus.clear();
                    for (int i = 0; i < statusList.size(); i++) {
                        stringBuilder.append(statusArray[statusList.get(i)]);
                        requestStatus.add(statusArray[statusList.get(i)]);
                        if (i != statusList.size() - 1) {
                            stringBuilder.append(", ");
                        }
                        statusSelection.setText(stringBuilder.toString());
                    }
                    setAdapter(requestStatus, apartments);
                } else {
                    setAdapter(requestStatus, apartments);
                }

            }
        }).setNegativeButton("Atšaukti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Išvalyti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedStatuses.length; i++) {
                    selectedStatuses[i] = false;
                    statusList.clear();
                    statusSelection.setText("");
                }
                requestStatus.add("Išspręstas");
                requestStatus.add("Atšauktas");
                requestStatus.add("Užregistruotas");
                setAdapter(requestStatus, apartments);
            }
        });
        builder.show();
    }

    private void setAdapter(List<String> requestStatus, List<String> apartments) {
        progressDialog.show();
        RequestController requestController = new RequestController();
        requestController.getRequestsByParameters(requestStatus, apartments, new OnGetDataListener<ArrayList<Request>>() {
            @Override
            public void onSuccess(ArrayList<Request> data) {
                setOnClickListener(data, requestStatus, apartments);
                AdminRequestRecyclerAdapter adapter = new AdminRequestRecyclerAdapter(getContext(), listener, data);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);

            }
        });
    }

    private void setOnClickListener(ArrayList<Request> data, List<String> requestStatus, List<String> apartments) {

        listener = new AdminRequestRecyclerAdapter.AdminRecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                AdminRequestInformationFragment requestInformationFragment = new AdminRequestInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("apartments", (ArrayList<String>) apartments);
                bundle.putString("requestStatus", String.valueOf(requestStatus));
                bundle.putString("requestDescription", data.get(position).getRequestDescription());
                bundle.putString("requestStatus", data.get(position).getRequestStatus());
                bundle.putString("requestDate", data.get(position).getRegistrationDate());
                bundle.putString("fullName", data.get(position).getSenderFullName());
                bundle.putString("address", data.get(position).getApartmentBuilding());
                bundle.putString("requestId", data.get(position).getRequestId());
                bundle.putString("phoneNumber", data.get(position).getPhoneNumber());
                bundle.putString("Url", data.get(position).getImage());
                requestInformationFragment.setArguments(bundle);
                Navigation.findNavController(v).navigate(R.id.action_nav_admin_request_to_nav_admin_request_information, bundle);

            }
        };
    }
}