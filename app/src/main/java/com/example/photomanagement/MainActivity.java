package com.example.photomanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.OnDialogButtonClickListener;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String path;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);

        TakePermission();
    }

    private void TakePermission() {



        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // fetch images

                        Toast.makeText(MainActivity.this, "permission granted!", Toast.LENGTH_SHORT).show();

                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            String[] projection   ={MediaStore.MediaColumns.DATA};

                            Cursor cursor = getContentResolver().query(uri,projection,null,null,null);

                            Log.d("cursor",cursor.toString()+  "");

                            while (cursor.moveToNext()){
                               path =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                                stringArrayList.add(path);

                                Log.d("path",path +"");
                            }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,stringArrayList);
                         listView.setAdapter(arrayAdapter);

                         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                             @Override
                             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                                     Log.d("values",stringArrayList.get(i) + "");

                                     Intent intent = new Intent(MainActivity.this,Image.class);
                                     intent.putExtra("image",stringArrayList.get(i));
                                     startActivity(intent);

                             }
                         });

//                           Log.d("images",stringArrayList + "");

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Galary Permission")
                                .setMessage("This permission is required to fetch images.")
                                .setPositiveButton("GOTO Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();

                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.fromParts("package",getPackageName(),null));
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                            builder.show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }
}