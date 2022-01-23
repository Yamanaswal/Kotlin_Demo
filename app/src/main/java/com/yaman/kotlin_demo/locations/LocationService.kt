package com.yaman.kotlin_demo.locations

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.location.SettingsClient
import com.google.android.gms.location.LocationServices
import android.widget.Toast

import android.app.Activity

import com.google.android.gms.location.LocationSettingsStatusCodes

import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.common.api.ApiException

import android.content.IntentSender.SendIntentException
import android.util.Log

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