package com.asadbek.googlemaprealtimeexample2


import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.asadbek.googlemaprealtimeexample2.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker

const val TAG = "MapsActivity"
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private lateinit var binding: ActivityMapsBinding

    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()

        // qancha vaqtda real time joylashuvni belgilab turishi
        locationRequest.setInterval(1000)
        locationRequest.setFastestInterval(1000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack, Looper.getMainLooper())


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }
    var marker:Marker? = null
    val locationCallBack = object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            if (null == p0){
                return
            }
            for (location:Location in p0.locations){
                Log.d(TAG, "onLocationResult: ${location.toString()}")
                if (mMap != null){
                    val cameraPosition = CameraPosition.Builder().target(LatLng(location.latitude,location.longitude)).bearing(location.bearing).zoom(10f).build()
                    mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    if (marker == null){
                        marker = mMap?.addMarker(MarkerOptions().position(LatLng(location.latitude,location.longitude)))
                    }else{
                        marker?.position = LatLng(location.latitude,location.longitude)
                    }

                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
    }
}