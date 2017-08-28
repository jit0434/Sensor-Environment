package com.example.papa.sensor_environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.LegendRenderer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.graphics.Color;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.*;
import android.content.Intent;
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
import android.view.*;


public class Main2Activity extends AppCompatActivity implements SensorEventListener {
    private final String TAG = "GraphSensors";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private GraphView mGraphGyro;
    private GraphView mGraphAccel;
    private GraphView mGraphLI;
    private GraphView mGraphMag;
    private LineGraphSeries<DataPoint> mSeriesGyro;
    private LineGraphSeries<DataPoint> mSeriesAccel;
    private LineGraphSeries<DataPoint> mSeriesMag;
    private LineGraphSeries<DataPoint> mSeriesLI;
    private double graphLastGyroXValue = 5d;
    private double graphLastAccelXValue = 5d;
    private double graphLastLIXValue = 5d;
    private double graphLastMagXValue = 5d;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //TODO make this onCreate method not suck and subclass graphs and series
        mGraphGyro = initGraph(R.id.graphGyro, "Sensor.TYPE_GYROSCOPE");
        mGraphAccel = initGraph(R.id.graphAccel, "Sensor.TYPE_ACCELEROMETER");
        mGraphLI = initGraph(R.id.graphLI, "Sensor.TYPE_LIGHT");
        mGraphMag = initGraph(R.id.graphMag, "Sensor.TYPE_MAGNETIC_FIELD");
        back = (Button)findViewById(R.id.back);
        //back.setOnClickListener(new View.OnClickListener());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //GYRO
        mSeriesGyro = initSeries(Color.BLUE, "Gyro");
        mGraphGyro.addSeries(mSeriesGyro);
        startGyro();

        // ACCEL
        mSeriesAccel = initSeries(Color.BLUE, "Accel");
        mGraphAccel.addSeries(mSeriesAccel);
        startAccel();


        // LI
        mSeriesLI = initSeries(Color.RED, "Light Int");
        mGraphLI.addSeries(mSeriesLI);
        startLI();

        // Magnatic
        mSeriesMag = initSeries(Color.BLUE, "Magne");
        mGraphMag.addSeries(mSeriesMag);
        startMag();
    }

    public GraphView initGraph(int id, String title) {
        GraphView graph = (GraphView) findViewById(id);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getGridLabelRenderer().setLabelVerticalWidth(100);
        graph.setTitle(title);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        return graph;
    }

    public LineGraphSeries<DataPoint> initSeries(int color, String title) {
        LineGraphSeries<DataPoint> series;
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        series.setDrawBackground(false);
        series.setColor(color);
        series.setTitle(title);
        return series;
    }

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

    public void startLI(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
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
            graphLastGyroXValue += 0.15d;
            double gx,gy,gz, gyro;
            gx = event.values[0];
            gy = event.values[1];
            gz = event.values[2];
            gyro = Math.sqrt(gx * gx + gy * gy + gz * gz);
            mSeriesGyro.appendData(new DataPoint(graphLastGyroXValue, gyro), true, 33);
        } else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            graphLastAccelXValue += 0.15d;
            double ax,ay,az, accel;
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            accel = Math.sqrt(ax * ax + ay * ay + az * az);
            mSeriesAccel.appendData(new DataPoint(graphLastAccelXValue, accel), true, 33);

        } else if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
            graphLastLIXValue += 0.15d;
            mSeriesLI.appendData(new DataPoint(graphLastLIXValue, event.values[0]), true, 33);
        } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            graphLastMagXValue += 0.15d;
            double mx,my,mz, magne;
            mx = event.values[0];
            my = event.values[1];
            mz = event.values[2];
            magne = Math.sqrt(mx * mx + my * my + mz * mz);
            mSeriesMag.appendData(new DataPoint(graphLastMagXValue,magne ), true, 33);

        }
        String dataString = String.valueOf(event.accuracy) + "," + String.valueOf(event.timestamp) + "," + String.valueOf(event.sensor.getType()) + "\n";
        Log.d(TAG, "Data received: " + dataString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged called for the gyro");
    }


}

