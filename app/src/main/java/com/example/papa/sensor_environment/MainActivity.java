package com.example.papa.sensor_environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView LI, Accel, Gyro, Magno;
    private Button Btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LI = (TextView)findViewById(R.id.temp);
        Accel = (TextView)findViewById(R.id.accel);
        Gyro = (TextView)findViewById(R.id.gyro);
        Magno = (TextView)findViewById(R.id.magne);
        Btn = (Button)findViewById(R.id.button);

        startTemp();
        startAccel();
        startGyro();
        startMag();
    }


    public void startTemp(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);}

    public void startGyro(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startAccel(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startMag(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        //event.values[x,y,z]
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            double gx,gy,gz, gyro;
            gx = event.values[0];
            gy = event.values[1];
            gz = event.values[2];
            gyro = Math.sqrt(gx * gx + gy * gy + gz * gz);
            Gyro.setText("Gyroscope: " + gyro);

        } else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double ax,ay,az, accel;
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            accel = Math.sqrt(ax * ax + ay * ay + az * az);
            Accel.setText("Acceleration: " + accel);

        }
        else if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            LI.setText("Temperature: " + event.values[0]);

        } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            double mx,my,mz, magne;
            mx = event.values[0];
            my = event.values[1];
            mz = event.values[2];

            magne = Math.sqrt(mx * mx + my * my + mz * mz);
            Magno.setText("Mangntometer: " + magne);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}