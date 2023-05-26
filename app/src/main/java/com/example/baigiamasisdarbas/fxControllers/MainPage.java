package com.example.baigiamasisdarbas.fxControllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.UserController;
import com.example.baigiamasisdarbas.ds.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


public class MainPage extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        NavigationUI.setupWithNavController(navigationView, navController);
        logout = findViewById(R.id.imageLogOut);
        findViewById(R.id.imageLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainPage.this, Login.class));
                finish();
            }
        });

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("userType");
        }
        if (value.equals("ADMIN")) {
            logout.setImageResource(R.drawable.baseline_input_24);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.admin_activity_main_page_drawer);
        }


        if (value.equals("ADMIN")) {
            navController.setGraph(R.navigation.admin_mobile_navigation);
        } else {
            NavigationHeader();
            navController.setGraph(R.navigation.mobile_navigation);
        }
        final TextView textTitle = findViewById(R.id.textTitle);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                textTitle.setText(navDestination.getLabel());
            }
        });


    }

    public void NavigationHeader() {
        NavigationView navigationView2 = findViewById(R.id.navigationView);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_main_page, null);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        UserController userController = new UserController();
        userController.getCurrentUser(new OnGetDataListener<User>() {
            @Override
            public void onSuccess(User user) {
                ((TextView) nav_header.findViewById(R.id.textNameHeader)).setText(user.getName() + " " + user.getSurname());
                ((TextView) nav_header.findViewById(R.id.textEmailHeader)).setText(user.getEmail());

                ImageView imageView = nav_header.findViewById(R.id.imageView);
                Picasso.get().load(user.getPictureUri()).into(imageView);

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
            }
        });

        navigationView2.addHeaderView(nav_header);
    }


}