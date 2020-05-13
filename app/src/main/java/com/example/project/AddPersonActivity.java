package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPersonActivity extends AppCompatActivity implements View.OnClickListener, NfcAdapter.CreateNdefMessageCallback {

    private static final int PICK_IMAGE = 2;
    private static String TAG = "AddPersonActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 9;
    public static AddPersonActivity addPersonActivity;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person_activity);


        addPersonActivity = this;
        ImageButton scanButton = findViewById(R.id.addPerson);
        ImageButton generateQRButton = findViewById(R.id.generateNewQr);
        ImageButton arrowBackButton = findViewById(R.id.arrowBackButton);
        ImageButton connectNFCButton = findViewById(R.id.nfcButton);


        scanButton.setOnClickListener(this);
        arrowBackButton.setOnClickListener(this);
        generateQRButton.setOnClickListener(this);
        connectNFCButton.setOnClickListener(this);

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter != null) {
            connectNFCButton.setOnClickListener(this);
            mAdapter.setNdefPushMessageCallback(this, this);
        }
        else {
            connectNFCButton.setVisibility(View.GONE);
        }

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

                EditText friendName = findViewById(R.id.friendName);
                EditText ourName = findViewById(R.id.ourName);
                if(friendName.getText().toString().length()>20 || friendName.getText().toString().length()<=0 || ourName.getText().toString().length()>20 || ourName.getText().toString().length()<=0)
                {
                    Toast.makeText(this,"Name is too long or too short",Toast.LENGTH_SHORT).show();
                    return;
                }
                NewPerson newPerson = new NewPerson(friendName.getText().toString(),ourName.getText().toString());

                Gson gson = new Gson();
                SendAbleMessage sendAbleMessage = new SendAbleMessage().setMessage(new Message(gson.toJson(newPerson)).setStatus(
                        3*(int)Math.pow(10,Integer.toString(newPerson.num).length())
                                +newPerson.num)).setIpFor(Utils.getIPAddress(true)).setIpFrom("");
                String message = gson.toJson(sendAbleMessage);

                Uri bmpUri = null;
                try {
                    bmpUri = QRCodeGenerator.getLocalBitmapUri(QRCodeGenerator.encodeAsBitmap(message, BarcodeFormat.QR_CODE, 500, 500), activity);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                if (bmpUri != null) {
                    try {
                        //bmpUri = new Uri(bmpUri.getPath());
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/*");
                        // Launch sharing dialog for image
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something gone wrong", Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.arrowBackButton:
                onBackPressed();
                break;

            case R.id.nfcButton:

                break;

        }

    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }



    /*@Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            String msg = new String(message.getRecords()[0].getPayload());

            Gson gson = new Gson();
            SendAbleMessage sendAbleMessage = gson.fromJson(msg,SendAbleMessage.class);
            ClientStarter.execute(sendAbleMessage.getIpFor(), sendAbleMessage.getMessage(),3);

        } //else
           // mTextView.setText("Waiting for NDEF Message");

    }*/

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {


        EditText friendName = findViewById(R.id.friendName);
        EditText ourName = findViewById(R.id.ourName);
        if(friendName.getText().toString().length()>20 || friendName.getText().toString().length()<=0 || ourName.getText().toString().length()>20 || ourName.getText().toString().length()<=0)
        {
            //Toast.makeText(this,"Name is too long or too short",Toast.LENGTH_SHORT).show();
            return null;
        }
        NewPerson newPerson = new NewPerson(friendName.getText().toString(),ourName.getText().toString());

        Gson gson = new Gson();
        SendAbleMessage sendAbleMessage = new SendAbleMessage().setMessage(new Message(gson.toJson(newPerson)).setStatus(
                3*(int)Math.pow(10,Integer.toString(newPerson.num).length())
                        +newPerson.num)).setIpFor(Utils.getIPAddress(true)).setIpFrom("");
        String message = gson.toJson(sendAbleMessage);


        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }




    void save() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MainActivity.SAVE_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SaveLoader saveLoader = new SaveLoader(MainActivity.dialogues,InvitesHead.invitesHead);
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
