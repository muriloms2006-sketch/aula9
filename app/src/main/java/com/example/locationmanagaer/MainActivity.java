package com.example.locationmanagaer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jspecify.annotations.NonNull;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView=findViewById(R.id.textView);
        MapView mapView=findViewById(R.id.mapview);
        locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(getPackageName());

        //verifica se a permissão já foi concluida.
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //define provider
            Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            textView.setText(l.getLatitude()+","+l.getLongitude());
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, location ->{
               textView.setText(location.getLatitude()+","+location.getLongitude());
               //inicia mapa
                mapView.getController().setZoom(18);
                mapView.getController().setCenter(
                        new org.osmdroid.util.GeoPoint(location.getLatitude(), location.getLongitude())
                );
                mapView.getOverlays().clear();
                Marker marker = new org.osmdroid.views.overlay.Marker(mapView);
                marker.setPosition(new org.osmdroid.util.GeoPoint(location.getLatitude(), location.getLongitude()));
                mapView.invalidate();
            });
        } else {
            //solicitar permissão em tempo de execução.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                }
            }
        }
    }
}