package com.example.photomanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class Image extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        imageView = findViewById(R.id.imageview);

        String intent = getIntent().getStringExtra("image");

        Picasso.get().load(new File(intent)).into(imageView);
    }
}