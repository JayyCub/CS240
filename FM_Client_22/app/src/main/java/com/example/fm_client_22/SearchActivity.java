package com.example.fm_client_22;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import DAO_Models.Event;
import DAO_Models.Person;

public class SearchActivity extends AppCompatActivity {
    private DataCache dataCache = DataCache.getInstance();
    private static final int EVENT_TYPE = 0;
    private static final int PERSON_TYPE = 1;
    SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        RecyclerView recyclerView = findViewById(R.id.recyclebox);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchBox);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.people.clear();
                for (String key: dataCache.people.keySet()) {
                    if (!dataCache.checkSearchFilter(key)){ continue; }
                    String dataString = dataCache.people.get(key).getFirstName().toLowerCase() +
                            dataCache.people.get(key).getLastName().toLowerCase();
                    newText = newText.replaceAll(" ", "");

                    if (dataString.contains(newText)){
                        adapter.people.add(dataCache.people.get(key));
                    }
                }
                adapter.events.clear();
                for (String key: dataCache.events.keySet()) {
                    Event event = dataCache.events.get(key);
                    if (!dataCache.checkFilter(event.getPersonID())){ continue; }
                    Person assocPerson = dataCache.people.get(event.getPersonID());
                    String dataString = assocPerson.getFirstName().toLowerCase() +
                            assocPerson.getLastName().toLowerCase() +
                            event.getEventType().toLowerCase() +
                            event.getCountry().replaceAll(" ", "").toLowerCase() +
                            event.getCity().replaceAll(" ", "").toLowerCase() +
                            event.getYear();
                    newText = newText.replaceAll(" ", "");
                    if (dataString.contains(newText)){
                        adapter.events.add(dataCache.events.get(key));
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        public List<Person> people = new ArrayList<>();
        public List<Event> events = new ArrayList<>();

        @RequiresApi(api = Build.VERSION_CODES.N)
        RecyclerAdapter(){
            for (String key: dataCache.people.keySet()) {
                if (!dataCache.checkSearchFilter(key)){ continue; }
                this.people.add(dataCache.people.get(key));
            }
            for (String key: dataCache.events.keySet()) {
                if (!dataCache.checkFilter(dataCache.events.get(key).getPersonID())){ continue; }
                this.events.add(dataCache.events.get(key));
            }
        }

        @Override
        public int getItemViewType(int position){
            return position < events.size() ? EVENT_TYPE : PERSON_TYPE;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == EVENT_TYPE) {
                view = getLayoutInflater().inflate(R.layout.recycler_event, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.recycler_person, parent, false);
            }
            return new RecyclerViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            if (position < events.size()){
                holder.bind(events.get(position));
            } else {
                position -= events.size();
                holder.bind(people.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int viewType;
        private Event event;
        private Person person;

        public RecyclerViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            itemView.setOnClickListener(this);
        }

        public void bind(Event event) {
            this.event = event;
            String type = event.getEventType().toLowerCase();
            TextView textView = itemView.findViewById(R.id.searchTitle);
            textView.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() +
                    " (" + event.getYear() + ")");
            textView = itemView.findViewById(R.id.eventFLName);
            textView.setText(dataCache.people.get(event.getPersonID()).getFirstName() + " " +
                    dataCache.people.get(event.getPersonID()).getLastName());

            ImageView imageView = itemView.findViewById(R.id.resultIcon);
            DataUtility utility = new DataUtility();
            imageView.setImageResource(utility.getMarker(type));
        }
        public void bind(Person person) {
            this.person = person;
            TextView title = itemView.findViewById(R.id.searchTitle);
            title.setText(person.getFirstName() + " " + person.getLastName());
            ImageView imageView = itemView.findViewById(R.id.resultIcon);
            if (Objects.equals(person.getGender(), "m")) {
                imageView.setImageResource(R.drawable.maleicon);
            } else {
                imageView.setImageResource(R.drawable.femaleicon);
            }
        }

        @Override
        public void onClick(View v) {
            if (viewType == EVENT_TYPE){
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                Gson gson = new Gson();
                String eventData = gson.toJson(event);
                intent.putExtra("eventString", eventData);
                SearchActivity.this.startActivity(intent);

            } else {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(person);
                intent.putExtra("personData", data);
                SearchActivity.this.startActivity(intent);
            }
        }
    }
}
