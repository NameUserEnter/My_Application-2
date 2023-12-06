package com.example.myapplication;

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
public class NetworkTask extends AsyncTask<Void, Void, Road> {

    private WeakReference<PersonActivity> activityReference;
    private MapView mapa;
    private ArrayList<GeoPoint> myPoints;

    NetworkTask(PersonActivity activity,MapView mapa, ArrayList<GeoPoint> myPoints) {
        activityReference = new WeakReference<>(activity);
        this.mapa = mapa;
        this.myPoints = myPoints;
    }

    @Override
    protected Road doInBackground(Void... voids) {
        RoadManager roadManager = new OSRMRoadManager(mapa.getContext(), "MyAwesomeMapApp/1.0");
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_BIKE);
        return roadManager.getRoad(myPoints);
    }

    @Override
    protected void onPostExecute(Road road) {
        PersonActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (road != null && road.mStatus == Road.STATUS_OK) {
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            activity.mapa.getOverlays().add(roadOverlay);
            activity.mapa.invalidate();
        } else {
        }
    }

}
