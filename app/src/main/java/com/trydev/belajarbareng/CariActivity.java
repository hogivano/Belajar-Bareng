package com.trydev.belajarbareng;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import database.Kelas;
import database.LokasiKelas;
import database.User;

public class CariActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener
{

    private GoogleMap mMap;


    //play service
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager lm;

    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference ref;
    DatabaseReference tableKelas;
    GeoFire geoFire;

    Marker mCurrent;
    Marker kelas;

    MaterialAnimatedSwitch locationSwitch;
    SupportMapFragment mapFragment;

    DatabaseReference tableLokasiKelas;
    FirebaseDatabase ref2;

    DatabaseReference cariUser;
    FirebaseDatabase ref3;

    private Dialog myDialogCari;

    ArrayList<Kelas> listKelas;

    Kelas get;
    String keyKelas;
    boolean cekLoadKelas;
    boolean cekDialog;

    TextView judul, destinasi, mapel, kapasitas, tanggal, jamMulai, jamSelesai;
    Button batal, bergabung;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps = false;
        boolean network = false;

        //init view
//        locationSwitch = (MaterialAnimatedSwitch) findViewById(R.id.locationSwitch);
//
//        locationSwitch.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isOnline) {
//                if (isOnline){
//                    startLocationUpdates();
//                    displayLocation();
//                    Toast.makeText(CariActivity.this, "Kamu sedang online", Toast.LENGTH_SHORT).show();
//                } else {
//                    stopLocationUpdates();
//                    mCurrent.remove();
//                    Toast.makeText(CariActivity.this, "Kamu sedang offline", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        try {
            gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e){

        }

        try {
            network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e){

        }

        if(!gps && !network) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Aktifkan Gps?");
            dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                    CariActivity.super.onBackPressed();
                }
            });

            dialog.show();
        }

        ref = FirebaseDatabase.getInstance().getReference("location");
        geoFire = new GeoFire(ref);
        setUpLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
//            if (mGoogleApiClient != null) {
//                mGoogleApiClient.connect();
//
//            }
//        }else{
//            // Showyourmesg();
//        }
//        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
//        }
//        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    if (checkPlayServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
//                        if (locationSwitch.isChecked()){
//                            displayLocation();
//                        }
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
//                if (locationSwitch.isChecked()){
//                    displayLocation();
//                }
                displayLocation();
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            } else {
                Toast.makeText(this, "Device tidak mendukung", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
//        Toast.makeText(this, String.valueOf(mGoogleApiClient), Toast.LENGTH_SHORT).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        
        if (mLastLocation != null){
//            if (locationSwitch.isChecked()){
                final double latitude = mLastLocation.getLatitude();
                final double longitude = mLastLocation.getLongitude();
                Log.d("TryDev", String.format("Lokasi berubah %f / %f", latitude, longitude));
                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        //marker
                        if (mCurrent != null){
                            mCurrent.remove(); //remove marker lama
                        }
                        mCurrent = mMap.addMarker(new MarkerOptions()
//                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationon))
                                                    .position(new LatLng(latitude, longitude))
                                                    .title("Anda"));
                        //update camera move camera
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));

                        //drawAnimaterMaker
//                        rotateMaker(mCurrent, 0, mMap);
                    }
                });
                loadLokasiKelas();
//            }
        } else {
            Log.d("ERROR", "Tidak bisa menemukan lokasi anda");
//            Toast.makeText(this, "Tidak bisa menemukan lokasi anda " + String.valueOf(mLastLocation), Toast.LENGTH_SHORT).show();
        }
    }

    private void rotateMaker(final Marker mCurrent, final float i, GoogleMap mMap) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = mCurrent.getRotation();
        final long duration = 1500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed/duration);
                float rot =  t * i + (1-t)*startRotation;
                int irot = (int) rot;
                mCurrent.setRotation(-irot > 180?irot/2:irot);

                if (t < 1.0){
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void loadLokasiKelas(){
        tableLokasiKelas = ref2.getInstance().getReference();
        tableLokasiKelas.child("lokasiKelas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()){
                    final LokasiKelas x = ds.getValue(LokasiKelas.class);

                    cariUser = ref3.getInstance().getReference();
                    cariUser.child("kelas").child(ds.getKey()).child("user").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean cek = false;
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                if (ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    cek = true;
                                    break;
                                }
                            }
                            if (!cek){
                                mMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationon))
                                        .position(new LatLng(x.getLangtitude(), x.getLongtitude()))
                                        .title(ds.getKey().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void callDetailKelas(Kelas x, String key){
        this.keyKelas = key;
        get = x;
        progressDialog = new ProgressDialog(this);

        myDialogCari = new Dialog(this);
        myDialogCari.setContentView(R.layout.activity_detail_kelas);

        judul = (TextView) myDialogCari.findViewById(R.id.judul);
        destinasi = (TextView) myDialogCari.findViewById(R.id.destinasi);
        mapel = (TextView) myDialogCari.findViewById(R.id.mapel);
        kapasitas = (TextView) myDialogCari.findViewById(R.id.kapasitas);
        tanggal = (TextView) myDialogCari.findViewById(R.id.tanggal);
        jamMulai = (TextView) myDialogCari.findViewById(R.id.jamMulai);
        jamSelesai = (TextView) myDialogCari.findViewById(R.id.jamSelesai);

        batal = (Button) myDialogCari.findViewById(R.id.batal);
        bergabung = (Button) myDialogCari.findViewById(R.id.bergabung);

        destinasi.setText(get.getDestinasi());
        judul.setText(get.getJudul());
        mapel.setText(get.getMapel());
        kapasitas.setText(get.getKapasitas());
        tanggal.setText(get.getHari() + "/" + get.getBulan() + "/" + get.getTahun());
        jamMulai.setText(get.getJamMulai() + ":" + get.getMenitSelesai());
        jamSelesai.setText(get.getJamSelesai() + ":" + get.getMenitSelesai());

        if (!isFinishing()){
            myDialogCari.show();
        } else {
            finish();
        }

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogCari.dismiss();
            }
        });

        bergabung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFinishing()) {
                    progressDialog.setMessage("Harap tunggu . . .");
                    progressDialog.show();
                }

                User baru = new User();
                get.insertUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), keyKelas);
                baru.InsertKelas(get, keyKelas, FirebaseAuth.getInstance().getCurrentUser().getUid());

                cekDialog = true;
                Toast.makeText(CariActivity.this, "Anda telah bergabung", Toast.LENGTH_SHORT).show();
                finish();
                myDialogCari.dismiss();
                CariActivity.super.onBackPressed();

                progressDialog.dismiss();
            }
        });


    }

//    public void loadKelas(){
//        tableKelas = FirebaseDatabase.getInstance().getReference();
//        listKelas = new ArrayList<>();
//        keyKelas = new ArrayList<>();
//
//        cekLoadKelas = false;
//
//        Log.e("Masuk LoadKelas", "True");
//
//        tableKelas.child("kelas").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()){
//                    Log.e("Loop Loadkelas", ds.getKey());
//                    listKelas.add(ds.getValue(Kelas.class));
//                    keyKelas.add(ds.getKey());
//                    key = ds.getKey();
//                    get = ds.getValue(Kelas.class);
//
//                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                        @Override
//                        public boolean onMarkerClick(Marker marker) {
//                            if (marker.getTitle().equals(key)){
////                                callDetailKelas(d);
//                                cekLoadKelas = true;
//                            }
//                            return true;
//                        }
//                    });
//                    if (cekLoadKelas){
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sby = new LatLng(-7.257187, 112.737121);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sby, 5.0f));

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    public void onBackPressed() {
        finish();
        stopLocationUpdates();
        super.onBackPressed();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.e("Marker", "Is Click");
        final boolean cek = false;
//        String title = marker.getTitle();
//        loadKelas();
//
//        for (int i = 0; i < keyKelas.size(); i++) {
//            Log.e("Loop", "OnMarkerClick");
//
//            if (keyKelas.get(i).equals(title)){
//                get = listKelas.get(i);
//                Log.e("ID Kelas", keyKelas.get(i) + "" + get.getDestinasi());
//                cek = true;
//                callDetailKelas();
//                break;
//            }
//        }
//
//        Log.e("Cek is", String.valueOf(cek));
//        Log.e("Cek is", String.valueOf(listKelas.size()));
        tableKelas = FirebaseDatabase.getInstance().getReference();
        tableKelas.child("kelas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (marker.getTitle().equals(ds.getKey())){
//                        get = ds.getValue(Kelas.class);
                        cekDialog = false;
                        callDetailKelas(ds.getValue(Kelas.class), ds.getKey());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return cek;
    }
}
