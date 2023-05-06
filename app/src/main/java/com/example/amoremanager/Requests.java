package com.example.amoremanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Requests extends AppCompatActivity {
    //variable declarations
    RecyclerView requestList;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbReference;
    String userId;
    FirebaseRecyclerOptions<Bookings> options;
    FirebaseRecyclerAdapter<Bookings, MyViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        //initialize variables
        requestList = findViewById(R.id.requestList);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Bookings").child(userId);

        requestList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        requestList.setHasFixedSize(true);

        //load data from database onto RecylerView
        loadData();
    }

    private void loadData() {
        options = new FirebaseRecyclerOptions.Builder<Bookings>().setQuery(dbReference, Bookings.class).build();
        adapter = new FirebaseRecyclerAdapter<Bookings, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Bookings model) {
                //load image
                Picasso.get().load(model.getImageUri()).into(holder.bookingImage);
                //set booked equipment name
                holder.bookingEquipName.setText(model.getEquipmentName());
                //set booking date
                holder.bookingDate.setText(model.getDate());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_view, parent, false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        requestList.setAdapter(adapter);
    }
}