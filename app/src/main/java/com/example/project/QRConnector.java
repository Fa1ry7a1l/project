package com.example.project;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QRConnector extends AppCompatActivity implements Runnable {
    private String TAG = "QRConnector";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private Context context;

    @Override
    public void run() {
        execute(requestCode,resultCode,data,context);
    }

    public QRConnector(int requestCode, int resultCode, Intent data, Context context) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        this.context = context;
    }

    private void execute(int requestCode, int resultCode, Intent data, Context context) {

        if (resultCode == RESULT_OK) {

            //Toast.makeText(this, "Result OK", Toast.LENGTH_SHORT).show();
            Uri selectedImageUri = data.getData();
            String selectedImagePath = //AddPersonActivity.getPath(selectedImageUri);
                    AddPersonActivity.addPersonActivity.getPath(selectedImageUri);
            Log.d(TAG, selectedImagePath);
            try {

                FileInputStream fileIs = new FileInputStream(selectedImagePath);
                BufferedInputStream bufferedStream = new BufferedInputStream(fileIs);
                byte[] bMapArray = new byte[bufferedStream.available()];
                bufferedStream.read(bMapArray);
                Bitmap myBitmap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);

                BarcodeDetector detector =
                        new BarcodeDetector.Builder(context)
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();
                if (!detector.isOperational()) {
                    //Toast.makeText(this, "Could not set up the detector!", Toast.LENGTH_SHORT).show();
                    if (fileIs != null) {
                        fileIs.close();
                    }
                    if (bufferedStream != null) {
                        bufferedStream.close();
                    }
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcode = detector.detect(frame);
                Barcode thisCode = barcode.valueAt(0);
                Log.d(TAG, "thisCode.rawValue is " + thisCode.rawValue);

                Gson gson = new Gson();
                SendAbleMessage sendAbleMessage = gson.fromJson(thisCode.rawValue, SendAbleMessage.class);
                if((int)((Integer.toString(sendAbleMessage.getMessage().getStatus())).charAt(0))-(int)('0') ==3)
                {
                    ClientStarter.execute(sendAbleMessage.getIpFor(), sendAbleMessage.getMessage(),3);
                }
                else {
                    ClientStarter.execute(sendAbleMessage.getIpFor(), sendAbleMessage.getMessage());
                }

                if (fileIs != null) {
                    fileIs.close();
                }
                if (bufferedStream != null) {
                    bufferedStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
