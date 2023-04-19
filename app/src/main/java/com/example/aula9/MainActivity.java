package com.example.aula9;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    TextView txtMsg;
    Button btnFoto, btnGravar;
    ImageView imgFoto;
    VideoView videoView;

    ActivityResultLauncher<Intent> fotografarLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result){
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imgBitmap = (Bitmap) extras.get("data");
                        imgFoto.setImageBitmap(imgBitmap);
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> gravarLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result){
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri videoUri = data.getData();
                        videoView.setVideoURI(videoUri);

                        MediaController media = new MediaController(MainActivity.this);
                        videoView.setMediaController(media);
                        videoView.start();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        btnFoto = (Button) findViewById(R.id.btnFoto);
        imgFoto = (ImageView) findViewById(R.id.imgFoto);
        videoView = (VideoView) findViewById(R.id.videoView);
        btnGravar = (Button) findViewById(R.id.btnGravar);


        if(getIntent().getStringExtra(Intent.EXTRA_TEXT) != null) {
            txtMsg.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fotografarLauncher.launch(intent);
            }
        });

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                gravarLauncher.launch(intent);
            }
        });
    }
}