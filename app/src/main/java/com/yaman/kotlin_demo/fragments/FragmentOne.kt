package com.yaman.kotlin_demo.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.yaman.kotlin_demo.R
import com.yaman.kotlin_demo.locations.GpsUtils
import com.yaman.kotlin_demo.locations.OnGpsListener


class FragmentOne : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.e("FragmentOne", "onCreateView: ")

        // Inflate the layout for this fragment
        GpsUtils(requireContext()).turnGPSOn(object : OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                // turn on GPS
//                isGPS = isGPSEnable
                Log.e("TAG", "gpsStatus: $isGPSEnable")
            }

        })

        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    private fun startLocationUpdates() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("FragmentOne", "onCreate: ")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        callLocationPermissions()

        setupLocationUpdates();

    }

    fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        return locationRequest;
    }

    private fun setupLocationUpdates() {

        val request = createLocationRequest();

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    Log.e("TAG", "onLocationResult: ${location.latitude}");
                    Log.e("TAG", "onLocationResult: ${location.longitude}");
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(request,
            locationCallback,
            Looper.getMainLooper())
    }


    private fun callLocationPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                    }
                    else -> {
                        // No location access granted.
                    }
                }
            }
        }

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("FragmentOne", "onAttach: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("FragmentOne", "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("FragmentOne", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("FragmentOne", "onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("FragmentOne", "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("FragmentOne", "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("FragmentOne", "onDetach: ")
    }

}