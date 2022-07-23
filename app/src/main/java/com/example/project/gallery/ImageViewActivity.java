package com.example.project.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.database.DatabaseImage;
import com.example.project.database.ImageElement;

import org.w3c.dom.Text;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    private String IMAGE_PATH = "IMAGE_PATH";
    ImageView image;

    TextView textName;
    TextView textDescription;
    TextView textPrice;

    DatabaseImage databaseImage;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extra = getIntent().getExtras();
        imagePath = extra.getString(IMAGE_PATH);

        databaseImage = new DatabaseImage(this);
        ImageElement imageElement = new ImageElement();
        imageElement = databaseImage.getImage(imagePath);

        textName = (TextView) findViewById(R.id.imageview_name);
        textDescription = (TextView) findViewById(R.id.imageview_desc);
        textPrice = (TextView) findViewById(R.id.imageview_price);


        image = (ImageView) findViewById(R.id.imageview_image);
        File imageFile = new File(imagePath);
        if(imageFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth(), myBitmap.getHeight(), true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            image.setImageBitmap(rotatedBitmap);
        }

        textName.setText(imageElement.getName());
        textDescription.setText(imageElement.getDescription());
        textPrice.setText(String.valueOf(imageElement.getPrice()));
    }

    public void onEditImage(View view) {
        Intent intent = new Intent(this, EditImageActivity.class);
        intent.putExtra("IMAGE_PATH", imagePath);
        startActivity(intent);
    }
}