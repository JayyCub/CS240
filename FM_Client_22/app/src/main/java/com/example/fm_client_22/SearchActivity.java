package com.example.fm_client_22;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import DAO_Models.Event;
import DAO_Models.Person;

public class SearchActivity extends AppCompatActivity {
    private DataCache dataCache = DataCache.getInstance();
    private static final int EVENT_TYPE = 0;
    private static final int PERSON_TYPE = 1;
    SearchView searchView;

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

        RecyclerAdapter(){
            for (String key: dataCache.people.keySet()) {
                this.people.add(dataCache.people.get(key));
            }
            for (String key: dataCache.events.keySet()) {
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
                view = getLayoutInflater().inflate(R.layout.event, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.person, parent, false);
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
            if (type.charAt(0) == 'a' | type.charAt(0) == 'b')
                imageView.setImageResource(R.drawable.blue2marker);
            else if (type.charAt(0) == 'c' | type.charAt(0) == 'd')
                imageView.setImageResource(R.drawable.bluemarker);
            else if (type.charAt(0) == 'e' | type.charAt(0) == 'f')
                imageView.setImageResource(R.drawable.greenmarker);
            else if (type.charAt(0) == 'g' | type.charAt(0) == 'h')
                imageView.setImageResource(R.drawable.lightbluemarker);
            else if (type.charAt(0) == 'i' | type.charAt(0) == 'j')
                imageView.setImageResource(R.drawable.lightpurplemarker);
            else if (type.charAt(0) == 'k' | type.charAt(0) == 'l')
                imageView.setImageResource(R.drawable.limemarker);
            else if (type.charAt(0) == 'm' | type.charAt(0) == 'n')
                imageView.setImageResource(R.drawable.navymarker);
            else if (type.charAt(0) == 'o' | type.charAt(0) == 'p')
                imageView.setImageResource(R.drawable.orangemarker);
            else if (type.charAt(0) == 'q' | type.charAt(0) == 'r')
                imageView.setImageResource(R.drawable.pinkmarker);
            else if (type.charAt(0) == 's' | type.charAt(0) == 't')
                imageView.setImageResource(R.drawable.purplemarker);
            else if (type.charAt(0) == 'u' | type.charAt(0) == 'v')
                imageView.setImageResource(R.drawable.redmarker);
            else if (type.charAt(0) == 'w' | type.charAt(0) == 'x')
                imageView.setImageResource(R.drawable.tealmarker);
            else if (type.charAt(0) == 'y' | type.charAt(0) == 'z')
                imageView.setImageResource(R.drawable.yellowmarker);

            switch (type) {
                case "birth":
                    imageView.setImageResource(R.drawable.birthmarker);
                    break;
                case "marriage":
                    imageView.setImageResource(R.drawable.marriagemarker);
                    break;
                case "death":
                    imageView.setImageResource(R.drawable.deathmarker);
                    break;

            }

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
                Toast.makeText(SearchActivity.this, event.getEventType() + " " +
                        event.getPersonID(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SearchActivity.this, person.getFirstName() + " " +
                        person.getLastName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
