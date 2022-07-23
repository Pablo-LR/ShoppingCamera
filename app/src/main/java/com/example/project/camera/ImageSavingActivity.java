package com.example.project.camera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.project.R;
import com.example.project.database.DatabaseImage;

import java.io.File;

public class ImageSavingActivity extends AppCompatActivity {

    private String IMAGE_PATH = "IMAGE_PATH";
    EditText nameText;
    EditText descriptionText;
    EditText priceText;
    ImageView imageView;
    Button saveButton;

    DatabaseImage databaseImage;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_saving);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseImage = new DatabaseImage(this);

        Bundle extra = getIntent().getExtras();
        imagePath = extra.getString(IMAGE_PATH);


        imageView = (ImageView) findViewById(R.id.imageview_image);
        File imageFile = new File(imagePath);
        if(imageFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth(), myBitmap.getHeight(), true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }


        nameText = (EditText) findViewById(R.id.image_saving_input_name);
        descriptionText = (EditText) findViewById(R.id.image_saving_input_desc);
        priceText = (EditText) findViewById(R.id.image_saving_input_price);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveImage(View view) {
        Log.d("Test", "HEY");
        databaseImage.insertImage(
                imagePath,
                nameText.getText().toString(),
                descriptionText.getText().toString(),
                Integer.parseInt(priceText.getText().toString()));
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageSavingActivity.this);

        builder.setCancelable(true);
        builder.setTitle("Discard Image");
        builder.setTitle("Do you want to discard this image?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new File(imagePath).delete();
                backActivity();
            }
        });

        builder.create();
        builder.show();
    }

    public void backActivity() {
        super.onBackPressed();
    }
}
