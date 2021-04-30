package com.test.ex0414;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private Button btn_picture;
    private Button btn_gallery;

    private final int REQUEST_TAKE_PICTURE = 1;
    private final int GET_GALLERY_IMAGE = 2;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.img);
        btn_picture = (Button) findViewById(R.id.btn_picture);
        btn_gallery = (Button) findViewById(R.id.btn_gallery);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }

        //카메라 촬영기능 구현
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivity(takePictureIntent);

                //사진 파일명과 임시 저장경로 정의
                try {
                    String fileName = "JPEG_"+System.currentTimeMillis();
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDir);
                    currentPhotoPath = imageFile.getAbsolutePath(); //사진파일의 절대경로 저장
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //사진파일에 대한 정보를 Uri로 접근하여 인텐트에 저장
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "com.test.ex0414.fileprovider", new File(currentPhotoPath));

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                //사진 촬영 후에 사진정보를 받아오기 위함
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        });

        //겔러리 불러오기
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GET_GALLERY_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //사진촬영 앱에 대한 요청결과 확인
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
            Uri picturePhotoURI = Uri.fromFile(new File(currentPhotoPath));
            img.setImageURI(picturePhotoURI);
            Log.v("카메라", picturePhotoURI.toString());
        //겔러리 요청에대한 결과 확인
        } else if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK) {
            //갤러리에서 선택한 사진 파일의 경로를 Uri 객체로 생성하여 이미지뷰에 적용
            Uri galleryURI = data.getData();
            img.setImageURI(galleryURI);
        }
    }
}