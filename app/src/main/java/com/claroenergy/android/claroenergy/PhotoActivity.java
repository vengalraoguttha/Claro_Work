package com.claroenergy.android.claroenergy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PhotoActivity extends AppCompatActivity {

    Button takePhotoButton,uploadPhotoButton;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE=2;
    private Bitmap bitmap;
    ImageView imageView;
    LinearLayout l1,l2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        takePhotoButton=(Button)findViewById(R.id.photo_take);
        uploadPhotoButton=(Button)findViewById(R.id.photo_upload);
        imageView=(ImageView)findViewById(R.id.photo_image);
        l1=(LinearLayout)findViewById(R.id.photo_option_screen);
        l2=(LinearLayout)findViewById(R.id.upload_screen);
    }

    public void photoTake(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);

                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally{
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
                 Bundle extras = data.getExtras();
                 Bitmap imageBitmap = (Bitmap) extras.get("data");
                 imageView.setImageBitmap(imageBitmap);
            }
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.VISIBLE);
    }

    public void photoUpload(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void serverUpload(View v){
        //upload work
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.GONE);
    }

    public void cancel(View v){
        l2.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);
    }

}
