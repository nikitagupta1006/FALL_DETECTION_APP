package com.example.nikita.falldetectionapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class FallDetect extends AppCompatActivity {

        private SensorManager sensorManager;
        Button mButtonOk;
        long mTime, mCurrentTime;
        int mCount;
        boolean mFallDetect;
        boolean falsePositive = false;
        float mValue, mThreshold, g = 9.81f;
        float[] mAccelerometerData, mMagnetometerData, mEarthAcceleration, mGravity, mRotationMatrix, mInverse;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mTime = System.currentTimeMillis();
            mEarthAcceleration=new float[16];
            mInverse=new float[16];
            mCount=0;
            mButtonOk = (Button)findViewById(R.id.ButtonOk);
            mButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFallDetect = false;
                    falsePositive = false;
                }
            });

            mFallDetect=false;
            mThreshold=60/g;
            sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.registerListener(sensorlistener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(sensorlistener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(sensorlistener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_FASTEST);
            if(mCurrentTime >= mTime + 30000 && mFallDetect && !falsePositive) {
                // send Message and alarm
            }
        }

        SensorEventListener sensorlistener = new SensorEventListener() {

            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                try {
                    switch (sensor.getType())
                    {
                        case Sensor.TYPE_MAGNETIC_FIELD:
                            mMagnetometerData = event.values;
                            break;
                        case Sensor.TYPE_GRAVITY:
                            mGravity = event.values;
                            mGravity[3] = 0;
                            break;
                    }
                    if ((mGravity != null) && (mMagnetometerData != null)
                            && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {
                        mAccelerometerData = event.values;
                        mAccelerometerData[3] = 0;
                        mValue = mAccelerometerData[2];
                    }
                    SensorManager.getRotationMatrix(mRotationMatrix, null, mGravity, mMagnetometerData);

                    android.opengl.Matrix.invertM(mInverse, 0, mRotationMatrix, 0);
                    android.opengl.Matrix.multiplyMV(mEarthAcceleration, 0, mInverse, 0, mAccelerometerData, 0);
                    if (mValue > mThreshold) {
                        mCount++;
                        mTime = System.currentTimeMillis();
                    }
                    if(mCurrentTime == mTime + 30000) {
                        if(mCount >=1) {
                            if(mValue<mThreshold) {
                                falsePositive = true;
                                mFallDetect=true;
                            }
                            else
                                mFallDetect=false;

                        }

                    }
                }catch (Exception e) {
                    Log.e("error", e.toString());
                }
            }
        };
        public void onStop() {
            super.onStop();
            sensorManager.unregisterListener(sensorlistener);
        }

    }