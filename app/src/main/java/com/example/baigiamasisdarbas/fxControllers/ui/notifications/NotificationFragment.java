package com.example.baigiamasisdarbas.fxControllers.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.RequestController;
import com.example.baigiamasisdarbas.ds.Request;
import com.example.baigiamasisdarbas.fxControllers.ui.request.RequestInformationFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationFragment extends Fragment {

    List<String> notifications = new ArrayList<>();
    List<String> notificationsID = new ArrayList<>();
    ListView list;
    TextView info;
    RequestController requestController = new RequestController();
    SharedPreferences sharedPreferences;
    Boolean notificationStatus = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        list = view.findViewById(R.id.listView);
        info = view.findViewById(R.id.notificationsInformationTextView);
        sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        checkRequests();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                requestController.getRequestById(notificationsID.get(position), new OnGetDataListener<Request>() {
                    @Override
                    public void onSuccess(Request data) {
                        RequestInformationFragment requestInformationFragment = new RequestInformationFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("requestDescription", data.getRequestDescription());
                        bundle.putString("requestStatus", data.getRequestStatus());
                        bundle.putString("requestDate", data.getRegistrationDate());
                        bundle.putString("apartment", data.getApartmentBuilding());
                        bundle.putString("phoneNumber", data.getPhoneNumber());
                        bundle.putString("name", data.getSenderFullName());
                        requestInformationFragment.setArguments(bundle);
                        Navigation.findNavController(v).navigate(R.id.action_nav_notification_to_nav_requests_information, bundle);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(notificationsID.get(position));
                        editor.apply();
                        notifications.remove(position);
                        notificationsID.remove(position);
                        checkRequests();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("Klaida", "Klaidos pranešimas: " + e);
                    }
                });

            }
        });
        return view;
    }

    public Boolean checkRequests() {
        sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        requestController.getAllRequestsByUser(new OnGetDataListener<ArrayList<Request>>() {
            @Override
            public void onSuccess(ArrayList<Request> data) {
                for (int i = 0; i < data.size(); i++) {
                    Boolean doesExist = sharedPreferences.contains(data.get(i).getRequestId());
                    if (doesExist) {
                        String modificationDate = sharedPreferences.getString(data.get(i).getRequestId(), null);
                        if (!Objects.equals(modificationDate, data.get(i).getRegistrationDate()) && modificationDate != null) {
                            System.out.println("AAAAAAAAAAAAAAAAAAAA" + modificationDate + "   " + data.get(i).getRequestId());
                            System.out.println("AAAAAAAAAAAAAAAAAAAs" + data.get(i).getRegistrationDate() + "   " + data.get(i).getRequestId());
                            notifications.add("Pasikeitė Jūsų užklausos, kurią užregistravote -  " + data.get(i).getRegistrationDate() + " statusas");
                            notificationsID.add(data.get(i).getRequestId());
                        }
                    }
                }
                fillList(notifications);
                if (notifications.size() > 0) {
                    notificationStatus = true;
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);

            }

        });
        return notificationStatus;
    }

    private void fillList(List<String> notifications) {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, notifications);
        list.setAdapter(listAdapter);
        if (notifications.size() == 0) {
            info.setText("Naujų pranešimų nėra");
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}