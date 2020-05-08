package com.example.project;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPersonActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 2;
    private static String TAG = "AddPersonActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 9;
    public static AddPersonActivity addPersonActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person_activity);
        addPersonActivity = this;
        ImageButton scanButton = findViewById(R.id.addPerson);
        ImageButton generateQRButton = findViewById(R.id.generateNewQr);
        ImageButton arrowBackButton = findViewById(R.id.arrowBackButton);

        scanButton.setOnClickListener(this);
        arrowBackButton.setOnClickListener(this);
        generateQRButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPerson:
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);

                break;

            case R.id.generateNewQr:
                break;
            case R.id.arrowBackButton:
                onBackPressed();
                break;

        }

    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }


    void save() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MainActivity.SAVE_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SaveLoader saveLoader = new SaveLoader(MainActivity.dialogues);
        editor.putString(MainActivity.SAVE_TAG, saveLoader.toString());
        editor.apply();
        Log.d(MainActivity.SAVE_TAG, "Everything saved");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"Starting QRConnector  stream");
        ExecutorService executorClient = Executors.newFixedThreadPool(1);
        QRConnector qrConnector = new QRConnector(requestCode, resultCode, data, this);
        executorClient.execute(qrConnector);

        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
