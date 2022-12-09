package com.example.fm_client_22;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import DAO_Models.Event;
import DAO_Models.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{
    private GoogleMap map;
    private DataCache dataCache = DataCache.getInstance();
    private LinearLayout bottomData;
    private LinearLayout mapLegend;
    private Map<Marker, Event> markerEventMap = new HashMap<>();
    private List<Polyline> mapLines = new ArrayList<>();
    TextView mapBotName;
    TextView mapBotSub;
    TextView mapBotSub2;
    private DataUtility utility = new DataUtility();


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
        placeMarkers();

        map.setOnMarkerClickListener(marker -> {

            for (Polyline line : mapLines){
                line.remove();
            }
            mapLines.clear();

            mapBotName = getView().findViewById(R.id.mapBotName);
            mapBotSub = getView().findViewById(R.id.mapBotSub);
            mapBotSub2 = getView().findViewById(R.id.mapBotSub2);
            bottomData = getView().findViewById(R.id.mapBotData);
            bottomData.setVisibility(View.VISIBLE);
            mapLegend = getView().findViewById(R.id.mapLegend);
            mapLegend.setVisibility(View.VISIBLE);

            Event event = markerEventMap.get(marker);
            ImageView icon = getView().findViewById(R.id.mapBotIcon);
            icon.setImageResource(utility.getMarker(event.getEventType()));

            Person person = dataCache.people.get(event.getPersonID());
            mapBotName.setText(person.getFirstName() + " " + person.getLastName());
            mapBotSub.setText(event.getEventType() + ", " + event.getYear());
            mapBotSub2.setText(event.getCity() + ", " + event.getCountry());

            icon = getView().findViewById(R.id.mapBotGenderIcon);
            if (Objects.equals(person.getGender(), "f")) {
                icon.setImageResource(R.drawable.femaleicon);
            } else {
                icon.setImageResource(R.drawable.maleicon);
            }
            // TODO: CREATE LINES
            // Spouse Line: Connect this event -> thisPerson -> spouse -> spouse birth
            // Family tree lines: this event -> father and mother birth -> recurse, get thinner
            // Life story: chronology of thisPerson, get all events based on current event.personID,
            //                              sort events by year, connect lines
            //      With all these, check settings filter to determine visibility

            if (dataCache.spouseSetting) createSpouseLine(event);
            if (dataCache.lifeSetting) createLifeStoryLines(event);
            if (dataCache.treeSetting) createFamilyLines(event);

            return false;
        });

        TextView textView = getView().findViewById(R.id.mapBotExit);
        textView.setOnClickListener(v -> {
            bottomData.setVisibility(View.GONE);
            mapLegend.setVisibility(View.GONE);
            for (Polyline line : mapLines){
                line.remove();
            }
            mapLines.clear();
        });

        if (getArguments().getString("eventString") != null){
            mapBotName = getView().findViewById(R.id.mapBotName);
            mapBotSub = getView().findViewById(R.id.mapBotSub);
            mapBotSub2 = getView().findViewById(R.id.mapBotSub2);
            bottomData = getView().findViewById(R.id.mapBotData);
            bottomData.setVisibility(View.VISIBLE);
            mapLegend = getView().findViewById(R.id.mapLegend);
            mapLegend.setVisibility(View.VISIBLE);

            Gson gson = new Gson();
            Event event = gson.fromJson(getArguments().getString("eventString"), Event.class);

            ImageView icon = getView().findViewById(R.id.mapBotIcon);
            icon.setImageResource(utility.getMarker(event.getEventType()));

            Person person = dataCache.people.get(event.getPersonID());
            mapBotName.setText(person.getFirstName() + " " + person.getLastName());
            mapBotSub.setText(event.getEventType() + ", " + event.getYear());
            mapBotSub2.setText(event.getCity() + ", " + event.getCountry());

            icon = getView().findViewById(R.id.mapBotGenderIcon);
            if (Objects.equals(person.getGender(), "f")) {
                icon.setImageResource(R.drawable.femaleicon);
            } else {
                icon.setImageResource(R.drawable.maleicon);
            }

            map.moveCamera(CameraUpdateFactory
                    .newLatLng(new LatLng(event.getLatitude(), event.getLongitude())));

            // TODO: CREATE LINES
            createSpouseLine(event);
            createLifeStoryLines(event);
            createFamilyLines(event);
        }
    }

    public void placeMarkers(){
        if (dataCache.events.size() == 0 | dataCache.people.size() == 0){
            ServerProxy serverProxy = new ServerProxy(dataCache.serverHost, dataCache.port);
            serverProxy.getAllPersonData();
        }

        for (String key: dataCache.events.keySet()){
            Event event = dataCache.events.get(key);
            if (dataCache.checkFilter(event.getPersonID())) {
                String type = event.getEventType().toLowerCase();
                BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(utility.getMarker(type));
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(event.getLatitude(), event.getLongitude()))
                        .icon(bitmap2));
                markerEventMap.put(marker, event);
            }

        }
    }

    public void createSpouseLine(Event baseEvent){
        Person baseEventPerson = dataCache.people.get(baseEvent.getPersonID());
        if ((Objects.equals(baseEventPerson.getGender(), "m") && !dataCache.femaleSetting) ||
            (Objects.equals(baseEventPerson.getGender(), "f") && !dataCache.maleSetting)) { return; }
        if (baseEventPerson.getSpouseID() != null){

            Person spouse = dataCache.people.get(baseEventPerson.getSpouseID());
            List<Event> spouseEvents = new ArrayList<>();
            for (String key : dataCache.events.keySet()){
                if (Objects.equals(dataCache.events.get(key).getPersonID(), spouse.getPersonID())){
                    spouseEvents.add(dataCache.events.get(key));
                }
            }
            Event spouseFirstEvent = spouseEvents.get(0);
            for (Event event : spouseEvents){
                if (event.getEventType().equalsIgnoreCase("birth")){
                    spouseFirstEvent = event;
                    break;
                } else if (event.getYear() < spouseFirstEvent.getYear()){
                    spouseFirstEvent = event;
                }
            }

            mapLines.add(map.addPolyline(new PolylineOptions().add(
                            new LatLng(baseEvent.getLatitude(), baseEvent.getLongitude()),
                            new LatLng(spouseFirstEvent.getLatitude(), spouseFirstEvent.getLongitude()))
                    .width(10).color(Color.rgb(125, 0, 171))));
        }
    }

    public void createLifeStoryLines(Event baseEvent){
        Person basePerson = dataCache.people.get(baseEvent.getPersonID());
        List<Event> basePersonEvents = new ArrayList<>();

        for (String key: dataCache.events.keySet()){
            Event event = dataCache.events.get(key);
            if (Objects.equals(event.getPersonID(), basePerson.getPersonID())){
                basePersonEvents.add(event);
            }
        }
        DataUtility util = new DataUtility();
        Event[] sortedEvents = util.sortEvents(basePersonEvents);

        for (int i = 1; i < sortedEvents.length; i++){
            mapLines.add(map.addPolyline(new PolylineOptions().add(
                            new LatLng(sortedEvents[i-1].getLatitude(), sortedEvents[i-1].getLongitude()),
                            new LatLng(sortedEvents[i].getLatitude(), sortedEvents[i].getLongitude()))
                    .width(8).color(Color.rgb(176, 0, 0))));
        }
    }

    public void createFamilyLines(Event baseEvent){
        Person person = dataCache.people.get(baseEvent.getPersonID());
        if (person.getMotherID() != null && dataCache.femaleSetting && dataCache.motherSetting) {
            //Get earliest event
            Person mother = dataCache.people.get(person.getMotherID());
            List<Event> motherEvents = new ArrayList<>();
            for (String key : dataCache.events.keySet()) {
                if (Objects.equals(dataCache.events.get(key).getPersonID(), mother.getPersonID())) {
                    motherEvents.add(dataCache.events.get(key));
                }
            }
            Event motherFirstEvent = motherEvents.get(0);
            for (Event event : motherEvents) {
                if (event.getEventType().equalsIgnoreCase("birth")) {
                    motherFirstEvent = event;
                    break;
                } else if (event.getYear() < motherFirstEvent.getYear()) {
                    motherFirstEvent = event;
                }
            }
            mapLines.add(map.addPolyline(new PolylineOptions().add(
                            new LatLng(baseEvent.getLatitude(), baseEvent.getLongitude()),
                            new LatLng(motherFirstEvent.getLatitude(), motherFirstEvent.getLongitude()))
                    .width(15).color(Color.rgb(255, 138, 189))));
            familyRecursive(motherFirstEvent, 15, Color.rgb(255, 138, 189));
        }

        if (person.getFatherID() != null && dataCache.maleSetting && dataCache.fatherSetting){
            //Get earliest event
            Person father = dataCache.people.get(person.getFatherID());
            List<Event> fatherEvents = new ArrayList<>();
            for (String key : dataCache.events.keySet()){
                if (Objects.equals(dataCache.events.get(key).getPersonID(), father.getPersonID())){
                    fatherEvents.add(dataCache.events.get(key));
                }
            }
            Event fatherFirstEvent = fatherEvents.get(0);
            for (Event event : fatherEvents){
                if (event.getEventType().equalsIgnoreCase("birth")){
                    fatherFirstEvent = event;
                    break;
                } else if (event.getYear() < fatherFirstEvent.getYear()){
                    fatherFirstEvent = event;
                }
            }
            mapLines.add(map.addPolyline(new PolylineOptions().add(
                            new LatLng(baseEvent.getLatitude(), baseEvent.getLongitude()),
                            new LatLng(fatherFirstEvent.getLatitude(), fatherFirstEvent.getLongitude()))
                    .width(15).color(Color.rgb(0, 110, 255))));
            familyRecursive(fatherFirstEvent, 15, Color.rgb(0, 110, 255));
        }


    }

    public void familyRecursive(Event primeEvent, int lineWidth, int color){
        lineWidth -= 3;
        Person person = dataCache.people.get(primeEvent.getPersonID());
        if (person.getMotherID() != null && dataCache.femaleSetting){
            //Get earliest event
            Person mother = dataCache.people.get(person.getMotherID());
            List<Event> motherEvents = new ArrayList<>();
            for (String key : dataCache.events.keySet()){
                if (Objects.equals(dataCache.events.get(key).getPersonID(), mother.getPersonID())){
                    motherEvents.add(dataCache.events.get(key));
                }
            }
            Event motherFirstEvent = motherEvents.get(0);
            for (Event event : motherEvents){
                if (event.getEventType().equalsIgnoreCase("birth")){
                    motherFirstEvent = event;
                    break;
                } else if (event.getYear() < motherFirstEvent.getYear()){
                    motherFirstEvent = event;
                }
            }

            mapLines.add(map.addPolyline(new PolylineOptions().add(
                            new LatLng(primeEvent.getLatitude(), primeEvent.getLongitude()),
                            new LatLng(motherFirstEvent.getLatitude(), motherFirstEvent.getLongitude()))
                    .width(lineWidth).color(color)));
            familyRecursive(motherFirstEvent, lineWidth, color);
        }
        if (person.getFatherID() != null && dataCache.maleSetting){
            //Get earliest event
            Person father = dataCache.people.get(person.getFatherID());
            List<Event> fatherEvents = new ArrayList<>();
            for (String key : dataCache.events.keySet()){
                if (Objects.equals(dataCache.events.get(key).getPersonID(), father.getPersonID())){
                    fatherEvents.add(dataCache.events.get(key));
                }
            }
            Event fatherFirstEvent = fatherEvents.get(0);
            for (Event event : fatherEvents){
                if (event.getEventType().equalsIgnoreCase("birth")){
                    fatherFirstEvent = event;
                    break;
                } else if (event.getYear() < fatherFirstEvent.getYear()){
                    fatherFirstEvent = event;
                }
            }

            mapLines.add(map.addPolyline(new PolylineOptions().add(
                            new LatLng(primeEvent.getLatitude(), primeEvent.getLongitude()),
                            new LatLng(fatherFirstEvent.getLatitude(), fatherFirstEvent.getLongitude()))
                    .width(lineWidth).color(color)));
            familyRecursive(fatherFirstEvent, lineWidth, color);
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
