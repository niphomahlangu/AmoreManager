package com.example.amoremanager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView, bookingImage;
    TextView imageTextView, bookingEquipName, bookingDate;
    View view;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        //items for single_view layout
        imageView = itemView.findViewById(R.id.imageView);
        imageTextView = itemView.findViewById(R.id.imageTextView);

        //items for request_view layout
        bookingImage = itemView.findViewById(R.id.bookingImage);
        bookingEquipName = itemView.findViewById(R.id.bookingEquipName);
        bookingDate = itemView.findViewById(R.id.bookingDate);
        view = itemView;
    }
}
