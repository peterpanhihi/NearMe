package com.example.peterpan.nearme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.peterpan.nearme.custom_listview.CustomListviewAdapter;
import com.example.peterpan.nearme.model.Bookmarks;
import com.example.peterpan.nearme.model.User;
import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Peterpan on 5/13/2016 AD.
 */
public class BookmarksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<Bookmarks> bookmarks;
    private User user;
    private String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        user_id = getIntent().getStringExtra("user_id");
        user = (User) getIntent().getSerializableExtra("user_object");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_name = (TextView)hView.findViewById(R.id.nav_name);
        nav_name.setText(user.getName());

        ImageView profileImgView = (ImageView) hView.findViewById(R.id.imageView_profile);
        Glide.with(BookmarksActivity.this)
                .load(user.getimageUrl())
                .into(profileImgView);

        bookmarks = new ArrayList<Bookmarks>();

        final ListView  listView = (ListView) findViewById(R.id.listview_bookmark);

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://nearmeapp.firebaseio.com/");

        ref.child("bookmarks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                bookmarks.clear();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Bookmarks b = postSnapshot.getValue(Bookmarks.class);
                    System.out.println(b.getName() + " - " + b.getType());
                    if(user_id.equalsIgnoreCase(b.getUser_id())) {
                        bookmarks.add(b);
                    }
                }

                System.out.println(Arrays.toString(bookmarks.toArray()));

                CustomListviewAdapter adapter = new CustomListviewAdapter(BookmarksActivity.this, bookmarks);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home) {
            Intent home = new Intent(BookmarksActivity.this, MainActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("user_object",user);
            home.putExtras(mBundle);
            home.putExtra("user_id", user_id);
            startActivity(home);
            finish();
        }

        if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            Intent login = new Intent(BookmarksActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
