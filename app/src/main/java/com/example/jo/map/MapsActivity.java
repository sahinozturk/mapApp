package com.example.jo.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private int konum_izni=200;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private LatLng konumum;

    // konumun üzerinde çıkan simge
    private Marker isaretci;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        if(ContextCompat.checkSelfPermission(this,Manifest.permission
        .ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

        // internetten https://snazzymaps.com/style/93 yeni bir map dizaynını entegre etmeye çalıştık ama
        //çalışmadı aşagıdaki kodun amacı silebilirsin hiç işe yaranıyor
       boolean stil= mMap.setMapStyle(new MapStyleOptions(getResources()
        .getString(R.string.map_style1)));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        int konum= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(konum!= PackageManager.PERMISSION_GRANTED)
        {
            // izin istegini dizinin içine atıyoruz
            String[] izinler={Manifest.permission.ACCESS_FINE_LOCATION};
            // İzin istegini ekrana çıkaran dialog bölmü
            ActivityCompat.requestPermissions(this,izinler,konum_izni);

        }
        else
        {
            konumlanir();
        }



    }


    private void konumlanir()
    {
        // telefonun sensöründen alğılanan veriyi alan metot bölümü

        locationManager =(LocationManager)getSystemService(LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Konumum olarak tanımladığım değişkene bilgileri aktarıyorum

                konumum=new LatLng(location.getLatitude(),location.getLatitude());

                // marker ı düzenliyoruz
                // eger orada bir marker varsa onu yok edeceğiz
                if(isaretci!= null)
                    isaretci.remove();
                // şimdi o konuma bizim istediğimiz işaretçiyi yani MAkerı koyabiliriz
                isaretci=mMap.addMarker(new MarkerOptions().position(konumum).title("şanda buradayız "));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(konumum,19));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle extras)
            {
                Log.i("konum",s+""+i);

            }

            @Override
            public void onProviderEnabled(String s)
            {
                Log.i("onProviderEnabled  :  ",s);

            }

            @Override
            public void onProviderDisabled(String s)
            {
                Log.i("onProviderDisable  :  ",s);

            }
        };


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == konum_izni)
        {
            // izin verilip verilmediğini kontrol ederiz
            boolean izinverildimi = grantResults[0] ==PackageManager.PERMISSION_GRANTED;

            if(izinverildimi)
                Toast.makeText(this,"uygulamayı kullan ", Toast.LENGTH_LONG).show();

        }





    }
}
