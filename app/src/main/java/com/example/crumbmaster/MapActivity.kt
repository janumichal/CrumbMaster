package com.example.crumbmaster

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


const val tag = "Debuging_TAG" // TODO remove later


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var PERMISSION_ID = 10

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    private fun scaleCircleAnim(context: Context){
        val menuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)
        menuBtn.hide()
        val displayMatrics = DisplayMetrics()
        // from android api 30
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            display?.getRealMetrics(displayMatrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMatrics)
        }
        val dHeight = displayMatrics.heightPixels
        val circle = findViewById<ImageView>(R.id.MenuCircle)
        val cHeight = circle.height
        val scaleVariable = dHeight/(cHeight/2) + 1

        val scaleY = ObjectAnimator.ofFloat(circle, "ScaleY", scaleVariable.toFloat())
        val scaleX = ObjectAnimator.ofFloat(circle, "ScaleX", scaleVariable.toFloat())
        scaleX.duration = 600
        scaleY.duration = 600

        val scaleUp = AnimatorSet()

        scaleUp.play(scaleX).with(scaleY)
        scaleUp.start()
        scaleUp.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?){}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                val intent = Intent(context, MenuActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.hold)
                recreate()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mMenuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)
        mMenuBtn.setOnClickListener{
            scaleCircleAnim(this)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        startTrackingPosition()
    }

    private fun getStreetName(lat:Double, long:Double){
        var streetName:String? = null
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address : MutableList<Address> = geoCoder.getFromLocation(lat, long, 1)

        streetName = address[0].thoroughfare
        if(streetName != null)
            Log.d("Debug:", "Ulica $streetName")
    }

    private fun startTrackingPosition() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 15000

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocation = p0.lastLocation
                val mark = LatLng(lastLocation.latitude, lastLocation.longitude)

                mMap.addMarker(MarkerOptions().position(mark).title("I'm here"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 18f))

                getStreetName(lastLocation.latitude, lastLocation.longitude)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

}