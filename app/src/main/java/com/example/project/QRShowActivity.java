package com.example.project;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class QRShowActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrshow_activity);
        imageView = findViewById(R.id.qrView);
        try {
            imageView.setImageBitmap(QRCodeGenerator.encodeAsBitmap("https://www.youtube.com/watch?v=dQw4w9WgXcQ", BarcodeFormat.QR_CODE,500,500));
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


}
