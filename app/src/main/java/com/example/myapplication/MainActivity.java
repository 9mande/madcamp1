package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int nCurrentPermission = 0;
    static final int PERMISSIONS_REQUEST = 0x0000001;
    private final int GALLERY_REQUEST_CODE = 50;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentTransaction ft;
    RVAdapter adapter;

    private SimpleDateFormat sdf, sdf2;
    private Date date;

    private String weather;
    private int temperature;

    private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] weathers = {"맑음", "비", "구름", "흐림"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        imageView.setImageResource(R.drawable.clicked_cat);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imageView.setImageResource(R.drawable.github_wh);
                        break;
                }
                return false;
            }
        });


        sdf = new SimpleDateFormat("yyyyMMdd HHmm");
        date = new Date(System.currentTimeMillis());
        String date_string = (String) sdf.format(date);
        String dates = date_string.split(" ")[0];
        String times = date_string.split(" ")[1];
        times = times.substring(0, 2) + "00";

        TextView look_when = findViewById(R.id.look_when);
        TextView look_temperature = findViewById(R.id.look_temperature);
        ImageView look_weather = findViewById(R.id.look_weather);

        sdf2 = new SimpleDateFormat("MM월 dd일 날씨");
        look_when.setText((String)sdf2.format(date));

        WeatherData weatherData = new WeatherData("67", "101", dates, times);
        Thread weatherThread = new Thread(){
            public void run(){
                int tmp = 0;
                try {
                    tmp = weatherData.getWeather();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                weather = weathers[tmp/100 - 1];
                temperature = tmp%100;
            }
        };
        weatherThread.start();
// Weather Thread 진행 동안 Cloth 불러옴

        try{
            weatherThread.join();

            if (weather == "맑음") {
                look_weather.setImageResource(R.drawable.sun);

            }else if (weather == "비") {
                look_weather.setImageResource(R.drawable.rain);

            }else if (weather == "구름") {
                look_weather.setImageResource(R.drawable.cloud);

            }else if (weather == "흐림") {
                look_weather.setImageResource(R.drawable.sun_cloud);
            }

            look_temperature.setText(temperature + "℃");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        Button home_button_phonebook = findViewById(R.id.home_button_phonebook);
        Button home_button_gallery = findViewById(R.id.home_button_gallery);
        Button home_button_todo = findViewById(R.id.home_button_todo);
        Button home_button_look = findViewById(R.id.home_button_look);

        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수

        home_button_phonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), activity_home.class);
                intent.putExtra("PAGE_KEY",0);
                //액티비티 시작!
                startActivity(intent);
            }
        });
        home_button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), activity_home.class);
                intent.putExtra("PAGE_KEY",1);
                //액티비티 시작!
                startActivity(intent);
            }
        });
        home_button_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), activity_home.class);
                intent.putExtra("PAGE_KEY",2);
                //액티비티 시작!
                startActivity(intent);
            }
        });
        home_button_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), activity_home.class);
                intent.putExtra("PAGE_KEY",3);
                //액티비티 시작!
                startActivity(intent);
            }
        });

        OnCheckPermission();
    }

    public void OnCheckPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                Toast.makeText(this, "Permission Request", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);

            }
        }
    }

    @Override
    @NonNull
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED) { // 0,1,2,3? all permission checked?
                    Toast.makeText(this, "Permisson Access Completed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }



    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
