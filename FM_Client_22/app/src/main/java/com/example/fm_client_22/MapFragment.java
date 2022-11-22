package com.example.fm_client_22;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import DAO_Models.Event;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private DataCache dataCache = DataCache.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));*/
        placeMarkers();
    }

    public void placeMarkers(){
        if (dataCache.events.size() == 0 | dataCache.people.size() == 0){
            ServerProxy serverProxy = new ServerProxy(dataCache.serverHost, dataCache.port);
            serverProxy.getAllPersonData();
        }

        for (String key: dataCache.events.keySet()){
            Event event = dataCache.events.get(key);
            String type = event.getEventType().toLowerCase();
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.birthmarker);
            if (type.charAt(0) == 'a' | type.charAt(0) == 'b') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.blue2marker);
            else if (type.charAt(0) == 'c' | type.charAt(0) == 'd') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bluemarker);
            else if (type.charAt(0) == 'e' | type.charAt(0) == 'f') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.greenmarker);
            else if (type.charAt(0) == 'g' | type.charAt(0) == 'h') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.lightbluemarker);
            else if (type.charAt(0) == 'i' | type.charAt(0) == 'j') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.lightpurplemarker);
            else if (type.charAt(0) == 'k' | type.charAt(0) == 'l') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.limemarker);
            else if (type.charAt(0) == 'm' | type.charAt(0) == 'n') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.navymarker);
            else if (type.charAt(0) == 'o' | type.charAt(0) == 'p') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orangemarker);
            else if (type.charAt(0) == 'q' | type.charAt(0) == 'r') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pinkmarker);
            else if (type.charAt(0) == 's' | type.charAt(0) == 't') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.purplemarker);
            else if (type.charAt(0) == 'u' | type.charAt(0) == 'v') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.redmarker);
            else if (type.charAt(0) == 'w' | type.charAt(0) == 'x') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.tealmarker);
            else if (type.charAt(0) == 'y' | type.charAt(0) == 'z') bitmap = BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker);

            switch (type) {
                case "birth": bitmap = BitmapDescriptorFactory.fromResource(R.drawable.birthmarker); break;
                case "marriage": bitmap = BitmapDescriptorFactory.fromResource(R.drawable.marriagemarker); break;
                case "death": bitmap = BitmapDescriptorFactory.fromResource(R.drawable.deathmarker); break;
            }
            map.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude()))
                    .title(dataCache.people.get(event.getPersonID()).getFirstName() + " " +
                            dataCache.people.get(event.getPersonID()).getLastName() + " - " +
                            event.getEventType())
                    .snippet(event.getCountry() + ", " + event.getCity())
                    .icon(bitmap));
        }
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

}
