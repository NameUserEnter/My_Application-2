package com.example.myapplication;

import static android.os.Build.VERSION_CODES.M;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonActivity extends AppCompatActivity {

    String item;
    MapView mapa;
    ArrayList<GeoPoint> myPoints= new ArrayList<>();

    private final int PERMISSAO_REQUERIDA = 1;
    private MyLocationNewOverlay mLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        if (Build.VERSION.SDK_INT >= M) {
            checkPermission();
        }
        Context ctx = getApplicationContext();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        Configuration.getInstance().load(ctx, preferences);
        Configuration.getInstance().setUserAgentValue("MyAwesomeMapApp/1.0");
        Configuration.getInstance().setCacheMapTileCount((short) 12);
        Configuration.getInstance().setCacheMapTileOvershoot((short) 12);
        mapa = (MapView) findViewById(R.id.map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);
        myPoints.clear();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            item = extras.getString("item");
            String[] parts = item.split("Адреса: ");
            GeoPoint loc = getGeoPointFromAddress(parts[1]);
            createmarker(loc,"Location");
        }

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),mapa);
        this.mLocationOverlay.enableMyLocation();
        mapa.getOverlays().add(this.mLocationOverlay);
        this.mLocationOverlay.runOnFirstFix(new Runnable() {
                @Override
                public void run() {
                    GeoPoint location = mLocationOverlay.getMyLocation();
                    if (location != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mapa.getTileProvider().clearTileCache();
                                createmarker(location,"My Location");
                                //showRouteOnMap();
                                performNetworkRequestAfterLocationFound(mapa,myPoints);
                            }

                        });
                    } else {
                    }
                }
            });
        IMapController controller = mapa.getController();
        controller.setCenter(new GeoPoint(49.32663424460594, 31.354600848305637));
        controller.setZoom(7);
    }

    private void performNetworkRequestAfterLocationFound(MapView mapa, ArrayList<GeoPoint> myPoints) {
        new NetworkTask(this, mapa, myPoints).execute();
    }
    public void createmarker(GeoPoint point, String title) {
        myPoints.add(point);
        Marker my_marker = new Marker(mapa);
        my_marker.setPosition(point);
        my_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        my_marker.setTitle(title);
        mapa.getOverlays().add(my_marker);
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissoes = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissoes, PERMISSAO_REQUERIDA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSAO_REQUERIDA
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSAO_REQUERIDA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();

                }
                else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private GeoPoint getGeoPointFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new GeoPoint(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}