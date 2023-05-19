package com.example.myapplicationmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    public float orientation_value,ACCELEROMETERVALUE;
    private long lastUpdate;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor accelerometer,proximitysensor,lightsensor,gyroscopesensor;
    private float lastX1,lastY1,lastZ1;
    private static final int ACCLEROMETER_THRESHOLD=10;
    private TextView textview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitysensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        gyroscopesensor = sensorManager.getDefaultSensor((Sensor.TYPE_ORIENTATION));
        textview3 = (TextView) findViewById(R.id.textView3);
    }
    @Override
    protected void onPostResume() {
            super.onPostResume();
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);


        }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float axisX=event.values[0];
            float axisY=event.values[1];
            float axisZ=event.values[2];
            orientation_value= axisX+axisZ+axisY;
            textview3.setText("Page Success \nOrientation sensor's current value is "+String.valueOf(orientation_value)
                    +"\nAxis X's current value is "+String.valueOf(axisX)
                    +"\nAxis Y's current value is "+String.valueOf(axisY)+
                    "\nAxis Z's current value is "+String.valueOf(axisZ)
                    +"\nAccelerometer sensor's current value is "+String.valueOf(ACCELEROMETERVALUE));
            if(ACCELEROMETERVALUE>150&&axisZ<10&&axisY<-20&&axisX>100&&orientation_value>20){
            Intent pageintent=new Intent(MainActivity2.this,
                   MainActivity.class);
            startActivity(pageintent);
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float l = event.values[0];
            float m = event.values[1];
            float n = event.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > 100) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                ACCELEROMETERVALUE = Math.abs(l + m + n - lastX1 - lastY1 - lastZ1) / diffTime * 10000;


                lastX1 = l;
                lastY1 = m;
                lastZ1 = n;

            }


        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}