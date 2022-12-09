package com.example.fm_client_22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import DAO_Models.Event;
import DAO_Models.Person;

public class PersonActivity extends AppCompatActivity {
    private final DataCache dataCache = DataCache.getInstance();
    private Person thisPerson;
    private TextView FName, LName, Gender;
    private ImageView icon;
    private ExpandableListView listView;
    public List<Person> people = new ArrayList<>();
    public List<Event> events = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
        Gson gson = new Gson();
        thisPerson = gson.fromJson(getIntent().getStringExtra("personData"), Person.class);

        FName = findViewById(R.id.userFName);
        FName.setText(thisPerson.getFirstName());

        LName = findViewById(R.id.userLName);
        LName.setText(thisPerson.getLastName());

        Gender = findViewById(R.id.userGender);
        icon = findViewById(R.id.genderIcon);
        if (Objects.equals(thisPerson.getGender(), "m")) {
            Gender.setText(R.string.male);
        } else {
            Gender.setText(R.string.female);
            icon.setImageResource(R.drawable.femaleicon);
        }

        getAssociatedData();
        listView = findViewById(R.id.expandableListView);
        listView.setAdapter(new ExpandanbleListAdapter(people, events));
    }

    private void getAssociatedData(){
        if (thisPerson.getMotherID() != null){
            people.add(dataCache.people.get(thisPerson.getMotherID()));
        }
        if (thisPerson.getFatherID() != null){
            people.add(dataCache.people.get(thisPerson.getFatherID()));
        }
        for (String key: dataCache.people.keySet()){
            Person person = dataCache.people.get(key);
            if (Objects.equals(Objects.requireNonNull(person).getFatherID(), thisPerson.getPersonID())
                    || Objects.equals(person.getMotherID(), thisPerson.getPersonID())
                    || Objects.equals(person.getSpouseID(), thisPerson.getPersonID())){
                people.add(person);
            }
        }

        for (String key: dataCache.events.keySet()){
            Event event = dataCache.events.get(key);
            if (Objects.equals(event.getPersonID(), thisPerson.getPersonID())){
                events.add(event);
            }
        }

        Event[] sortedEvents = events.toArray(new Event[0]);
        Event temp;
        for (int i = 0; i < sortedEvents.length; i++){
            for (int j = 0; j < sortedEvents.length - 1; j++){
                if (sortedEvents[j+1].getYear() < sortedEvents[j].getYear()){
                    temp = sortedEvents[j+1];
                    sortedEvents[j+1] = sortedEvents[j];
                    sortedEvents[j] = temp;
                }
            }
        }
        events = Arrays.asList(sortedEvents);
    }

    private class ExpandanbleListAdapter extends BaseExpandableListAdapter {
        private static final int People_POS = 0;
        private static final int Event_POS = 1;

        private final List<Person> peopleList;
        private final List<Event> eventList;

        private ExpandanbleListAdapter(List<Person> peopleList, List<Event> eventList) {
            this.peopleList = peopleList;
            this.eventList = eventList;
        }


        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case People_POS:
                    return peopleList.size();
                case Event_POS:
                    return eventList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // Not used
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // Not used
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View layoutPerson, ViewGroup parent) {
            if(layoutPerson == null) {
                layoutPerson = getLayoutInflater().inflate(R.layout.list_title, parent, false);
            }

            TextView titleView = layoutPerson.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case People_POS:
                    titleView.setText(R.string.listPeopleTitle);
                    break;
                case Event_POS:
                    titleView.setText(R.string.listEventsTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return layoutPerson;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case People_POS:
                    itemView = getLayoutInflater().inflate(R.layout.list_person, parent, false);
                    initPersonItem(itemView, childPosition);
                    break;
                case Event_POS:
                    itemView = getLayoutInflater().inflate(R.layout.recycler_event, parent, false);
                    initEventItem(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initPersonItem(View personView, final int childPosition) {
            TextView personName = personView.findViewById(R.id.personName);
            personName.setText(peopleList.get(childPosition).getFirstName() + " " +
            peopleList.get(childPosition).getLastName());

            TextView resortLocationView = personView.findViewById(R.id.personRelationship);
            ImageView icon = personView.findViewById(R.id.listIcon);
            Person person = peopleList.get(childPosition);
            DataUtility util = new DataUtility();
            resortLocationView.setText(util.relationship(thisPerson, person));

            if (Objects.equals(person.getGender(), "f")){
                icon.setImageResource(R.drawable.femaleicon);
            }

            personView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);

                Gson gson = new Gson();
                String data = gson.toJson(person);
                intent.putExtra("personData", data);

                PersonActivity.this.startActivity(intent);
            });
        }

        private void initEventItem(View personView, final int childPosition) {
            Event event = eventList.get(childPosition);
            TextView eventInfo = personView.findViewById(R.id.searchTitle);
            eventInfo.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() +
                    " (" + event.getYear() + ")");

            eventInfo = personView.findViewById(R.id.eventFLName);
            eventInfo.setText(dataCache.people.get(event.getPersonID()).getFirstName() + " "
            + dataCache.people.get(event.getPersonID()).getLastName());

            ImageView icon = personView.findViewById(R.id.resultIcon);
            DataUtility utility = new DataUtility();
            icon.setImageResource(utility.getMarker(event.getEventType().toLowerCase()));


            personView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                Gson gson = new Gson();
                String eventData = gson.toJson(event);
                intent.putExtra("eventString", eventData);
                PersonActivity.this.startActivity(intent);
            });
        }

            @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
