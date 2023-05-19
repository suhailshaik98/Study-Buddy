package com.example.myapplicationmonitor;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    MediaPlayer mediaPlayer;
    Button bluebutton,nextpage,references_button;
    AudioManager audiomanager;
    public static int BLUETOOTH_REQ_CODE=1;
    public float magnetometer_value,lightsensor_value,orientation_value,proximity_value,accelerometer_value;
    public void play(View view) {

        mediaPlayer.start();
    }

    public void pause(View view) {
        mediaPlayer.pause();
    }

    private TextView textview, textview2,proximitysensortext,lightsensortext,orientation,prompttext;
    private SensorManager sensorManager;
    public float gyroscope_value;
    private Sensor sensor;
    private Sensor accelerometer,proximitysensor,lightsensor,gyroscopesensor,rotationVectorSensor;
    private long lastUpdate = 0;
    private float lastX, lastY, lastZ, lastX1, lastY1, lastZ1;
    private static final int MAGNET_THRESHOLD = 400;
    private static final int ACCLEROMETER_THRESHOLD=10;
    private SensorEventListener proximitySensorListner;
    private BluetoothAdapter BA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.studymusic);
        audiomanager = (AudioManager) getSystemService(AUDIO_SERVICE);
        bluebutton = findViewById(R.id.bluebutton);
        references_button=findViewById((R.id.references_button));
        nextpage=findViewById(R.id.nextpage);
        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA == null) {
            Toast.makeText(MainActivity.this, "This Device doesnt support Bluetooth",
                    Toast.LENGTH_LONG).show();

        }
        if (!BA.isEnabled()) {
            bluebutton.setText("Turn Bluetooth ON");
        } else {
            bluebutton.setText("Turn Bluetooth OFF");
        }
        bluebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BA.isEnabled()) {
                    Intent BAIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(BAIntent, BLUETOOTH_REQ_CODE);
                } else {
                    BA.disable();
                    bluebutton.setText("Turn Bluetooth ON");

                }
            }
        });
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextpagefun();
            }

            public void nextpagefun() {
                Intent pageintent=new Intent(MainActivity.this,
                        MainActivity2.class);
                startActivity(pageintent);
            }
        });
        references_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextpagefun2();
            }

            public void nextpagefun2() {
                Intent pageintent2=new Intent(MainActivity.this,
                        MainActivity3.class);
                startActivity(pageintent2);
            }
        });

        textview = (TextView) findViewById(R.id.textView);
        textview2 = (TextView) findViewById(R.id.textView2);
        proximitysensortext = (TextView) findViewById((R.id.proximitysensortext));
        lightsensortext=(TextView)findViewById(R.id.lightsensortext);
        prompttext=(TextView)findViewById(R.id.textView4);
        orientation=(TextView)findViewById(R.id.orientation);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitysensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightsensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        gyroscopesensor=sensorManager.getDefaultSensor((Sensor.TYPE_GYROSCOPE));
        rotationVectorSensor =sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (proximitysensor == null) {
            Toast.makeText(this, "Proximity Sensor is not Available", Toast.LENGTH_SHORT).show();
            finish();
        }
        proximitySensorListner = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                proximitysensortext.setText(String.valueOf(sensorEvent.values[0]));

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }};


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Toast.makeText(MainActivity.this,"Bluetooth is ON",Toast.LENGTH_SHORT).show();
            prompttext.setText("Place the earphones in the ears to listen to some soothing music!");
            mediaPlayer.start();
            bluebutton.setText("Turn Bluetooth OFF");
        }else{
            if(resultCode==RESULT_CANCELED){
                Toast.makeText(MainActivity.this,"Bluetooth permission was denied",Toast.LENGTH_SHORT).show();
                prompttext.setText("Place your wireless earphone or case near the earpiece of the phone!");
                mediaPlayer.stop();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((this));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){

float axisX=event.values[0];
float axisY=event.values[1];
float axisZ=event.values[2];
orientation_value= axisX+axisZ+axisY;
            orientation.setText("The Orientation sensor's current values are \n"+
                    "X-Axis "+Float.toString(Math.round(axisX))+" degree \n"+
                    "Y-Axis "+Float.toString(Math.round(axisY))+" degree \n"+
                    "Z-Axis "+Float.toString(Math.round(axisZ))+" degree \n");
            if(axisX>150||axisX<10&&axisY<10){
            if (axisZ<-40||axisZ>50){
                orientation.setText("Current Orientation is Landscape and current value's are \n"+
                        "X-Axis "+Float.toString(Math.round(axisX))+" degree \n"+
                        "Y-Axis "+Float.toString(Math.round(axisY))+" degree \n"+
                        "Z-Axis "+Float.toString(Math.round(axisZ))+" degree \n");
                if(lightsensor_value<210&&accelerometer_value<10){
                    Intent pageintent=new Intent(MainActivity.this,
                            MainActivity2.class);
                    startActivity(pageintent);
                }
            }


        }else{
                orientation.setText("Current Orientation is Portrait sensor's and current values are \n"+
                "X-Axis "+Float.toString(Math.round(axisX))+" degree \n"+
                        "Y-Axis "+Float.toString(Math.round(axisY))+" degree \n"+
                        "Z-Axis "+Float.toString(Math.round(axisZ))+" degree \n");
            //}
        }}
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > 100) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                magnetometer_value = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;


                if (magnetometer_value > MAGNET_THRESHOLD) {
                    //mediaPlayer.start();
                    textview.setText(("The magnetometer's value is high" +
                            "\nX is " + String.valueOf(x)+" µT" +
                            "\nY is"+ String.valueOf(y)+" µT"+
                            "\nZ is"+ String.valueOf(z)+" µT"));


                }else{
                    textview.setText(("The magnetometer's value is low "+
                            "\nX is " + String.valueOf(x)+" µT" +
                            "\nY is"+ String.valueOf(y)+" µT"+
                            "\nZ is"+ String.valueOf(z)+" µT"));
                }
            }
            lastX = x;
            lastY = y;
            lastZ = z;}
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float l = event.values[0];
                float m = event.values[1];
                float n = event.values[2];

                long currentTime = System.currentTimeMillis();

                if ((currentTime - lastUpdate) > 100) {
                    long diffTime = (currentTime - lastUpdate);
                    lastUpdate = currentTime;

                    float speed = Math.abs(l + m + n - lastX1 - lastY1 - lastZ1) / diffTime * 10000;
                    accelerometer_value=speed;

                    if (speed > ACCLEROMETER_THRESHOLD/*&&gyroscope_value<30||gyroscope_value>200*/) {
                        //mediaPlayer.start();
                        textview2.setText("The accelerometer value is high, current value is "+
                                "\nX is " + String.valueOf(l)+" m/s^2" +
                                "\nY is"+ String.valueOf(m)+" m/s^2"+
                                "\nZ is"+ String.valueOf(n)+" m/s^2");
                        /*Intent pageintent=new Intent(MainActivity.this,
                                MainActivity.class);
                        startActivity(pageintent);*/


                        //put clock function

                    }else{
                        textview2.setText("The accelerometer's current value is "+
                                "\nX is " + String.valueOf(l)+" m/s^2" +
                                "\nY is"+ String.valueOf(m)+" m/s^2"+
                                "\nZ is"+ String.valueOf(n)+" m/s^2");

                    }
                    lastX1 = l;
                    lastY1 = m;
                    lastZ1 = n;

}


                }
            if (event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            proximity_value=event.values[0];

            if(magnetometer_value>MAGNET_THRESHOLD&&proximity_value<5){
                proximitysensortext.setText("Proximity Sensor sensing activity "+String.valueOf(proximity_value)+" cm");
                prompttext.setText("Sensed the earphone, please accept the bluetooth request to listen to the music");

                if (!BA.isEnabled()) {
                    Intent BAIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(BAIntent, BLUETOOTH_REQ_CODE);
                    prompttext.setText("Sensed the earphone, please accept the bluetooth request to listen to the music");


                }
            else{
            prompttext.setText("Place your wireless earphone or case near the earpiece of the phone! \nBluetooth is ON");
            }
            }else{
                proximitysensortext.setText("Proximity Sensor no activity "+String.valueOf(proximity_value)+" cm");

            }
            }if(event.sensor.getType()==Sensor.TYPE_LIGHT){
                lightsensor_value=event.values[0];
            if(lightsensor_value<10){
                lightsensortext.setText("Alert !, Sensing low level of light, Light Sensor sensing activity of value "+String.valueOf(lightsensor_value)+" lx");
            }else{
                lightsensortext.setText("Light Sensor sensing activity of value "+String.valueOf(lightsensor_value)+" lx")
                ;
            }
        }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}




