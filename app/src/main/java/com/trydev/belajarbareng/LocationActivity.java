package com.trydev.belajarbareng;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import controller.BuatKelas;

public class LocationActivity extends FragmentActivity implements
        OnMapReadyCallback,
        PlaceSelectionListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener{

    private GoogleMap mMap;
    private Marker myMarker;

//    private SupportMapFragment mapFragment;
    private SupportMapFragment mapFragment2;

    private Button lanjut;
    private String destinasi;
    private Dialog myDialog;
    private LatLng searchLatLng;

    private static String hari;
    private static String bulan;
    private static String tahun;

    private EditText judul, mapel, kapasitas;

    private static String jamMulai;
    private static String menitMulai;

    private static String jamSelesai;
    private static String menitSelesai;

    private static boolean cekClick;

    private static Button setTanggal;
    private static Button setWaktuMulai;
    private static Button setWaktuSelesai;

    private double langtitude;
    private double longtitude;

    private String idUser;

    ProgressDialog progressDialog;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        progressDialog = new ProgressDialog(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        lanjut = (Button) findViewById(R.id.lanjut);

        mapFragment.getMapAsync(this);
        lanjut.setOnClickListener(this);
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }

    public void goToLocation(double lat, double lng, float i){
        if (myMarker != null){
            myMarker.remove();
        }

        getDestinasi(lat, lng);
        langtitude = lat;
        longtitude = lng;

        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .snippet(destinasi)
                .title("lokasi belajar anda"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), i));
    }

    public void getDestinasi(double lat, double lng){
        Geocoder geoCoder =  new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geoCoder.getFromLocation(lat, lng, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                destinasi = listAddresses.get(0).getAddressLine(0);
            } else {
                destinasi = "";
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

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

        mMap.setOnMapClickListener(this);
        // Add a marker in Sydney and move the camera
        LatLng sby = new LatLng(-7.257187, 112.737121);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sby));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sby, 10.0f));
    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }


    @Override
    public void onPlaceSelected(Place place) {
        Toast.makeText(this, String.valueOf(place.getName()), Toast.LENGTH_SHORT).show();
        destinasi = place.getName().toString();
//        searchLatLng = place.getLatLng();
    }

    @Override
    public void onError(Status status) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                Log.i("TryDev", "Place: " + place.getName());
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                // TODO: Handle the error.
//                Log.i("TryDev", status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//
//    }

    @Override
    public void onMapClick(LatLng latLng){
        searchLatLng = latLng;
        goToLocation(latLng.latitude, latLng.longitude, 20.0f);
    }

    @Override
    public void onClick(View view) {
        if (view == lanjut){
            if (searchLatLng != null){
                callBuatKelasDialog();
//                setTanggal.setText(String.valueOf(LocationActivity.hari) + "/" + String.valueOf(LocationActivity.bulan) + "/" + String.valueOf(LocationActivity.tahun));
            } else {
                Toast.makeText(this, "Anda harus pilih lokasi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callBuatKelasDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.activity_buat);
        myDialog.setCancelable(false);

        TextView lokasi = (TextView) myDialog.findViewById(R.id.lokasi);
        lokasi.setText(destinasi);

        Button batal = (Button) myDialog.findViewById(R.id.batal);
        Button buat = (Button) myDialog.findViewById(R.id.buat);

        judul = (EditText) myDialog.findViewById(R.id.judul);
        mapel = (EditText) myDialog.findViewById(R.id.mapel);
        kapasitas = (EditText) myDialog.findViewById(R.id.kapasitas);

        setTanggal = (Button) myDialog.findViewById(R.id.setTanggal);
        setWaktuMulai = (Button) myDialog.findViewById(R.id.setWaktuMulai);
        setWaktuSelesai = (Button) myDialog.findViewById(R.id.setWaktuSelesai);

        hari = "";
        bulan = "";
        tahun = "";
        jamMulai = "";
        menitMulai = "";
        jamSelesai = "";
        menitSelesai = "";

        if (!isFinishing()) {
            myDialog.show();
        } else {
            finish();
        }

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        buat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuatKelas bk = new BuatKelas(destinasi, judul.getText().toString(), mapel.getText().toString(),
                        kapasitas.getText().toString(), String.valueOf(hari), String.valueOf(bulan),
                        String.valueOf(tahun), String.valueOf(jamMulai), String.valueOf(menitMulai),
                        String.valueOf(jamSelesai), String.valueOf(menitSelesai),
                        String.valueOf(langtitude), String.valueOf(longtitude));
                if (!isFinishing()) {
                    progressDialog.setMessage("Harap tunggu . . .");
                    progressDialog.show();
                }

                idUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                if (!bk.cekForm()){
                    bk.saveData(idUser);
                    Toast.makeText(LocationActivity.this, "Kelas tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                    myDialog.dismiss();
                    LocationActivity.super.onBackPressed();
                } else {
                    Toast.makeText(LocationActivity.this, "Semua harus diisi!!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

        setTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
//                Toast.makeText(LocationActivity.this, String.valueOf(hari), Toast.LENGTH_SHORT).show();
            }
        });

        setWaktuMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekClick = true;
                showTimePickerDialog();
            }
        });

        setWaktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekClick = false;
                showTimePickerDialog();
            }
        });

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            LocationActivity.hari = String.valueOf(day);
            LocationActivity.bulan = String.valueOf(month+1);
            LocationActivity.tahun = String.valueOf(year);
            setTanggal.setText(LocationActivity.hari + "/" + LocationActivity.bulan + "/" +
                    LocationActivity.tahun);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if (cekClick){
                LocationActivity.jamMulai = String.valueOf(hourOfDay);
                LocationActivity.menitMulai = String.valueOf(minute);
                setWaktuMulai.setText(LocationActivity.jamMulai + ":" + LocationActivity.menitMulai);
            } else {
                LocationActivity.jamSelesai = String.valueOf(hourOfDay);
                LocationActivity.menitSelesai = String.valueOf(minute);
                setWaktuSelesai.setText(LocationActivity.jamSelesai + ":" + LocationActivity.menitSelesai);
            }
        }
    }

    public void showDatePickerDialog (){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}