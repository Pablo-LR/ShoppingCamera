package com.example.project.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.database.DatabaseImage;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE_PERMISSION = 101;
    private String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    ImageButton captureButton;
    ImageButton backButton;
    PreviewView viewFinder;

    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        captureButton = findViewById(R.id.capture_button);
        viewFinder = findViewById(R.id.viewFinder);
        captureButton.setOnClickListener(this);

        backButton = findViewById(R.id.camera_back_button);
        backButton.setOnClickListener(this);

        if(allPermissionGranted()){
            initializeCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION);
            if(allPermissionGranted()){
                initializeCamera();
            } else {
                Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean allPermissionGranted() {
        for(String permission : REQUIRED_PERMISSIONS) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void initializeCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        //Camera Selector use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        //Preview use case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

        //Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture_button:
                capturePhoto();
                break;
            case R.id.camera_back_button:
                onBackPressed();
                break;
        }
    }

    private void capturePhoto() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/CameraXApp";
        File photoDir = new File(path);

        if(!photoDir.exists()) {
            photoDir.mkdir();
        }

        Date date = new Date();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";

        File photoFile = new File(photoFilePath);

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(CameraActivity.this, "Photo has been saved successfully", Toast.LENGTH_SHORT).show();

                        //This "refresh" the photos, workaround to display without restarting.
                        MediaScannerConnection.scanFile(CameraActivity.this, new String[] { photoFile.getPath() }, new String[] { "image/jpg" }, null);

                        Intent intent = new Intent(CameraActivity.this, ImageSavingActivity.class);
                        intent.putExtra("IMAGE_PATH", photoFile.getAbsolutePath());
                        startActivity(intent);

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this, "Photo NOT Took", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}