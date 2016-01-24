package com.cgp.saral.customviews;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.cgp.saral.myutils.AverageAngle;


public class Compass implements SensorEventListener ,LocationListener {
	private static final String TAG = "Compass";

	private LocationManager mLocationManager;
	private SensorManager sensorManager;
	private Sensor gsensor;
	private Sensor msensor;
	private float[] mGravity = new float[3];
	private float[] mGeomagnetic = new float[3];
	private float azimuth = 0f;
	private float currectAzimuth = 0;

	private float[] mValuesAccelerometer;
	private float[] mValuesMagneticField;
	private float[] mMatrixR;
	private float[] mMatrixI;
	private float[] mMatrixValues;


	/**
	 * minimum change of bearing (degrees) to notify the change listener
	 */
	private final double mMinDiffForEvent;

	/**
	 * minimum delay (millis) between notifications for the change listener
	 */
	private final double mThrottleTime;

	/**
	 * the change event listener
	 */
	/**
	 * angle to magnetic north
	 */
	private AverageAngle mAzimuthRadians;


	// compass arrow to rotate
	public ImageView arrowView = null;

	/**
	 * Current GPS/WiFi location
	 */
	private Location mLocation;

	public Compass(Context context) {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		mMatrixR = new float[9];
		mMatrixI = new float[9];
		mMatrixValues = new float[3];

		mMinDiffForEvent = .5;
		mThrottleTime = 50;

		mAzimuthRadians = new AverageAngle(10);
	}

	public void start() {
		sensorManager.registerListener(this, gsensor,
				SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
		sensorManager.registerListener(this, msensor,
				SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

		for (final String provider : mLocationManager.getProviders(true)) {
			if (LocationManager.GPS_PROVIDER.equals(provider)
					|| LocationManager.PASSIVE_PROVIDER.equals(provider)
					|| LocationManager.NETWORK_PROVIDER.equals(provider)) {
				if (mLocation == null) {
					mLocation = mLocationManager.getLastKnownLocation(provider);
				}
				mLocationManager.requestLocationUpdates(provider, 0, 100.0f, this);
			}
		}
	}

	public void stop() {
		sensorManager.unregisterListener(this);
		mLocationManager.removeUpdates(this);
	}

	private void adjustArrow() {
		if (arrowView == null) {
		//	Log.i(TAG, "arrow view is not set");
			return;
		}

		//Log.i(TAG, "will set rotation from " + currectAzimuth + " to "
		//		+ azimuth);

		Animation an = new RotateAnimation(-currectAzimuth, -azimuth,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		currectAzimuth = azimuth;

		an.setDuration(50);
		an.setRepeatCount(0);
		an.setFillAfter(true);

		arrowView.startAnimation(an);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.97f;

		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
						* event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
						* event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
						* event.values[2];

				// mGravity = event.values;

				// Log.e(TAG, Float.toString(mGravity[0]));
			}

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				// mGeomagnetic = event.values;

				mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
						* event.values[0];
				mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
						* event.values[1];
				mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
						* event.values[2];
				// Log.e(TAG, Float.toString(event.values[0]));

			}

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				// Log.d(TAG, "azimuth (rad): " + azimuth);
				azimuth = (float) Math.toDegrees(orientation[0]); // orientation
				azimuth = (azimuth + 360) % 360;
				// Log.d(TAG, "azimuth (deg): " + azimuth);
				adjustArrow();
			}
		}



	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}


	@Override
	public void onLocationChanged(Location location) {

		// set the new location
		//this.mLocation = location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}


	public double getBearingForLocation(Location location)
	{
		return azimuth + getGeomagneticField(location).getDeclination();
	}

	private GeomagneticField getGeomagneticField(Location location)
	{
		GeomagneticField geomagneticField = new GeomagneticField(
				(float)location.getLatitude(),
				(float)location.getLongitude(),
				(float)location.getAltitude(),
				System.currentTimeMillis());
		return geomagneticField;
	}
}
