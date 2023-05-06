package com.example.amoremanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class InventoryManager extends AppCompatActivity {
    EditText txtSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<Equipment> options;
    FirebaseRecyclerAdapter<Equipment, MyViewHolder> adapter;
    DatabaseReference DataRef;
    FirebaseAuth firebaseAuth;
    String currentUser, empId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_manager);

        txtSearch = findViewById(R.id.txtSearch);
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        /*firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser().getUid();*/
        DataRef = FirebaseDatabase.getInstance().getReference().child("Equipment");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryManager.this,AddEquipment.class));
            }
        });

        loadData();
    }

    private void loadData() {
        options = new FirebaseRecyclerOptions.Builder<Equipment>().setQuery(DataRef, Equipment.class).build();
        adapter = new FirebaseRecyclerAdapter<Equipment, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Equipment model) {
                holder.imageTextView.setText(model.getEquipmentName());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
                //click list item
                /*holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(InventoryManager.this,Book.class);
                        intent.putExtra("EquipmentKey",getRef(holder.getPosition()).getKey());
                        startActivity(intent);
                    }
                });*/
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}