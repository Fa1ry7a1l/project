package com.example.project;

import androidx.appcompat.app.AppCompatActivity;


public class QRReader extends AppCompatActivity {
   /* private static final int SELECT_PICTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 9;
    private String  selectedImagePath;

    private final int PICK_IMAGE = 2;
    private Uri fileUri;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        imageView = findViewById(R.id.image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            Toast.makeText(this, "Result OK", Toast.LENGTH_SHORT).show();
            //if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            try {

                FileInputStream fileis = new FileInputStream(selectedImagePath);
                BufferedInputStream bufferedstream = new BufferedInputStream(fileis);
                byte[] bMapArray = new byte[bufferedstream.available()];
                bufferedstream.read(bMapArray);
                Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
                //Here you can set this /Bitmap image to the button background image
                imageView.setImageBitmap(bMap);

                if (fileis != null) {
                    fileis.close();
                }
                if (bufferedstream != null) {
                    bufferedstream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/
}
