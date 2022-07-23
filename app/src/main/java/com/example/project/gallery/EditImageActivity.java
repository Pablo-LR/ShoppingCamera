package com.example.project.gallery;

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
import android.widget.TextView;

import com.example.project.R;
import com.example.project.camera.ImageSavingActivity;
import com.example.project.database.DatabaseImage;
import com.example.project.database.ImageElement;

import java.io.File;

public class EditImageActivity extends AppCompatActivity {

    private String IMAGE_PATH = "IMAGE_PATH";
    EditText nameText;
    EditText descriptionText;
    EditText priceText;
    ImageView imageView;

    DatabaseImage databaseImage;
    ImageElement imageElement;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extra = getIntent().getExtras();
        imagePath = extra.getString(IMAGE_PATH);

        databaseImage = new DatabaseImage(this);
        imageElement = new ImageElement();
        imageElement = databaseImage.getImage(imagePath);

        imageView = (ImageView) findViewById(R.id.edit_image);
        File imageFile = new File(imagePath);
        if(imageFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth(), myBitmap.getHeight(), true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }


        nameText = (EditText) findViewById(R.id.edit_input_name);
        descriptionText = (EditText) findViewById(R.id.edit_input_desc);
        priceText = (EditText) findViewById(R.id.edit_input_price);

        nameText.setText(imageElement.getName(), TextView.BufferType.EDITABLE);
        descriptionText.setText(imageElement.getDescription(), TextView.BufferType.EDITABLE);
        priceText.setText(String.valueOf(imageElement.getPrice()), TextView.BufferType.EDITABLE);

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

    public void onEditImage(View view) {
        databaseImage.insertImage(
                imagePath,
                nameText.getText().toString(),
                descriptionText.getText().toString(),
                Integer.parseInt(priceText.getText().toString()));
        super.onBackPressed();
    }

    public void onDeleteImage(View view) {
        new File(imagePath).delete();

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditImageActivity.this);

        builder.setCancelable(true);
        builder.setTitle("Discard Changes");
        builder.setTitle("Do you want to discard the changes?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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