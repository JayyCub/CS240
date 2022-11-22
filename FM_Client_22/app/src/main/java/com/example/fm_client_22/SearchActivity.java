package com.example.fm_client_22;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import DAO_Models.Event;
import DAO_Models.Person;

public class SearchActivity extends AppCompatActivity {
    private DataCache dataCache = DataCache.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        RecyclerView recyclerView = findViewById(R.id.recyclebox);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Person> people;
        private List<Event> events;

        RecyclerAdapter(){
            for (String key: dataCache.people.keySet()) {
                this.people.add(dataCache.people.get(key));
            }
            for (String key: dataCache.events.keySet()) {
                this.events.add(dataCache.events.get(key));
            }
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.event, parent, false);
            return new RecyclerViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private final String data1;
        private final int viewType;

        public RecyclerViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            itemView.setOnClickListener(this);
        }

        public void bind(){
            TextView title = itemView.findViewById(R.id.textView69);
            title.setText("Bruh momentum");
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(SearchActivity.this, "Some text stuff", Toast.LENGTH_SHORT).show();
        }
    }

}
