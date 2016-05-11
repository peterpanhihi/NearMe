package com.example.peterpan.nearme;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.peterpan.nearme.CustomSpinner.CustomAdapter;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        AdapterView.OnItemSelectedListener,
        GoogleMap.OnMarkerClickListener {

    /**
     * Request code for location permission request.
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleApiClient client;
    private GoogleMap mMap;
    private Location location;
    private OkHttpClient okHttpClient;
    private Gson gson;

    private Spinner spinner;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private final List<Marker> mMarkers = new ArrayList<Marker>();
    private List<Place> places;
    private ListTypes listTypes = new ListTypes();

    private User user;
    private String[] types;
    private String selectedType;
    private int selectedPos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Current Location", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showCurrentLocation();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = User.getInstance();
        View hView =  navigationView.getHeaderView(0);
        TextView nav_name = (TextView)hView.findViewById(R.id.nav_name);
        nav_name.setText(user.getName());

        ImageView profileImgView = (ImageView) hView.findViewById(R.id.imageView_profile);
        Glide.with(MainActivity.this)
                .load(user.getImageUrl())
                .into(profileImgView);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();

        okHttpClient = new OkHttpClient();
        gson = new Gson();

        spinner = (Spinner) findViewById(R.id.types_spinner);
        types = getResources().getStringArray(R.array.types_array);
        TypedArray ar = getResources().obtainTypedArray(R.array.icon_types_array);
        int len = ar.length();

        int[] icon_types = new int[len];

        for (int i = 0; i < len; i++)
            icon_types[i] = ar.getResourceId(i, 0);

        ar.recycle();

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), types, icon_types);
        spinner.setAdapter(customAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(customAdapter.getCount());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerClickListener(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (client.isConnected()) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            } else {
                LocationRequest req = new LocationRequest();
                req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                req.setInterval(2000);
                LocationServices.FusedLocationApi.requestLocationUpdates(client, req, this);

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            location = LocationServices.FusedLocationApi.getLastLocation(client);
            showCurrentLocation();

            LocationRequest req = new LocationRequest();
            req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            req.setInterval(2000);

            LocationServices.FusedLocationApi.requestLocationUpdates(client, req, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedType = types[pos].toLowerCase();
        selectedPos = pos;

        System.out.print("SELECTED TYPE : "+selectedType);
        if(selectedType.contains("beauty")) {
            selectedType = "beauty_salon";
        } else if(selectedType.contains("bus")) {
            selectedType = "bus_station";
        } else if(selectedType.contains("convenience")) {
            selectedType = "convenience_store";
        } else if(selectedType.contains("department")) {
            selectedType = "department_store";
        } else if(selectedType.contains("gas")) {
            selectedType = "gas_station";
        }else if(selectedType.equalsIgnoreCase("supermarket")) {
            selectedType = "grocery_or_supermarket";
        }

        if(!selectedType.equalsIgnoreCase("select place")){
            try {
                onClearMap();
                run();
                Toast.makeText(this, selectedType, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Another interface callback
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        Toast.makeText(this,marker.getTitle(),Toast.LENGTH_SHORT).show();

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_custom, null);
        builder.setView(view);

        final TextView name = (TextView) view.findViewById(R.id.locationName);
        final TextView address = (TextView) view.findViewById(R.id.address);
        final TextView phone = (TextView) view.findViewById(R.id.phone);
        final TextView website = (TextView) view.findViewById(R.id.website);

        builder.setPositiveButton("Route", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        int index = mHashMap.get(marker);
        Place place = places.get(index);

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place.getPlace_id() + "&key=AIzaSyCT0CYdLPDkwbNzUbLGZOALqiTRK9wOl38";

        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String jsonData = response.body().string();
                System.out.println(jsonData);
                JSONObject Jobject = null;
                JSONObject results;
                try {
                    Jobject = new JSONObject(jsonData);
                    results = Jobject.getJSONObject("result");
                    name.setText(results.getString("name"));
                    address.setText(results.getString("formatted_address"));
                    if(jsonData.contains("formatted_phone_number"))
                        phone.setText(results.getString("formatted_phone_number"));
                    if(jsonData.contains("website")) {
                        website.setText(results.getString("website"));
                        website.setTextColor(getResources().getColor(R.color.colorPrimary));
                        website.setLinksClickable(true);
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return false;
    }

    public void run() throws Exception {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+location.getLatitude()+","+location.getLongitude()+"&radius=500&types="+selectedType+"&key=AIzaSyCT0CYdLPDkwbNzUbLGZOALqiTRK9wOl38";
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String jsonData = response.body().string();
                JSONObject Jobject = null;
                String results ="";
                try {
                    Jobject = new JSONObject(jsonData);
                    results = Jobject.getJSONArray("results").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Type t = new TypeToken<List<Place>>(){}.getType();
                places = gson.fromJson(results, t);

                for (Place p: places) {
                    p.setType(selectedType);
                }

                listTypes.setList(selectedType, places);
                System.out.println(listTypes.toString());

                addMarkers();
            }
        });
    }

    public void showCurrentLocation() {
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()),15));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(17)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    public void addMarkers() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!checkReady()) {
                    return;
                }

                for (int i = 0; i < places.size(); i++) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    places.get(i).getGeometry().getLocation().getLat(),
                                    places.get(i).getGeometry().getLocation().getLng()))
                            .title(places.get(i).getName())
                            .icon(getMarkerIcon(getResources().getStringArray(R.array.color_type_array)[selectedPos])));
                    mMarkers.add(marker);
                    mHashMap.put(marker, i);
                }
            }
        });
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public void onClearMap() {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
        mMarkers.clear();
        mHashMap.clear();
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
