package com.yaman.kotlin_demo.locations

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.location.SettingsClient
import com.google.android.gms.location.LocationServices
import android.widget.Toast

import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.Intent

import com.google.android.gms.location.LocationSettingsStatusCodes

import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.common.api.ApiException

import android.content.IntentSender.SendIntentException
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yaman.kotlin_demo.R
import android.app.NotificationManager

import android.app.NotificationChannel


class LocationService : Service() {

    val CHANNEL_ID = "LocationServiceForeground"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel();
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.drawable.icon)
                .setTicker(getText(R.string.ticker_text))
                .build()
        } else {
            Log.e("onStartCommand", "sadsad")
            return START_NOT_STICKY
        }

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}


class GpsUtils(private val context: Context) {

    private var mSettingsClient: SettingsClient? = null
    private var mLocationSettingsRequest: LocationSettingsRequest
    private var locationManager: LocationManager? = null
    private lateinit var locationRequest: LocationRequest
    private val TAG = "GpsUtils"


    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mSettingsClient = LocationServices.getSettingsClient(context)
        locationRequest = LocationRequest.create()


        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = (2 * 1000).toLong()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }


    // method for turn on GPS
    fun turnGPSOn(onGpsListener: OnGpsListener) {
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener.gpsStatus(true)
        } else {
            mSettingsClient
                ?.checkLocationSettings(mLocationSettingsRequest)
                ?.addOnSuccessListener((context as Activity?)!!) { //  GPS is already enable, callback GPS status through listener
                    onGpsListener.gpsStatus(true)
                }
                ?.addOnFailureListener((context as Activity?)!!
                ) { e ->
                    val statusCode = (e as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(context as Activity, 10012)
                        } catch (sie: SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                            Log.e(TAG, errorMessage)
                            Toast.makeText(context as Activity?, errorMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
        }
    }


}

interface OnGpsListener {
    fun gpsStatus(isGPSEnable: Boolean)
}