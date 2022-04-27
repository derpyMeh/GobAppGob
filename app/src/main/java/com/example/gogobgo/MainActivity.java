package com.example.gogobgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView tv_score,stepDistance;
    private double MagnitudePrevious = 0;
    int steppieCount = 0;
    boolean running;
    Button b_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running = true;
        setContentView(R.layout.activity_main);
        tv_score = (TextView) findViewById(R.id.step_view);
        stepDistance =findViewById(R.id.step_distance);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        b_end = (Button) findViewById(R.id.b_end);

        b_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("lastScore", steppieCount);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(),BestActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(running){

            if(sensorEvent != null){
                float x_accel = sensorEvent.values[0];
                float y_accel = sensorEvent.values[1];
                float z_accel = sensorEvent.values[2];

                double Magnitude = Math.sqrt(x_accel*x_accel+y_accel*y_accel+z_accel*z_accel);
                double MagnitudeDelta = Magnitude - MagnitudePrevious;
                MagnitudePrevious = Magnitude;

                if(MagnitudeDelta > 6){
                    steppieCount++;
                }
                tv_score.setText(String.valueOf(steppieCount));
            }
           stepDistance.setText(String.valueOf(distanceCalc(steppieCount)));
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this,"Sensor Not found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            sensorManager.unregisterListener(this,sensor);

        }
    }

    public float distanceCalc(int steppieCount){
        float distance = (float)(steppieCount*78)/(float)100000;
        return distance;
        }

}

