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
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var mapLoaded: Boolean = false
    var firstLook:Boolean = true

    private var PERMISSION_ID = 10
    var myCircle: Circle? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    var gpsStatus: Boolean = false
    var coordinates: Array<LatLng>? = emptyArray()
    var backgroundEnabled: Boolean = true

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
        val scaleVariable = dHeight/(cHeight/2) + 2.5

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
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            val scaleYY = ObjectAnimator.ofFloat(circle, "ScaleY", 0.5.toFloat())
            val scaleXX = ObjectAnimator.ofFloat(circle, "ScaleX", 0.5.toFloat())
            scaleX.duration = 600
            scaleY.duration = 600

            val scaleDown = AnimatorSet()

            scaleDown.play(scaleXX).with(scaleYY)
            scaleDown.start()
        }, 1000)

    }

    @Throws(IOException::class)
    private fun loadJsonFromAssets(file : String): String{
        val inStr = this.assets?.open(file)
        val ret = ""
        if (inStr != null){
            val size = inStr.available()
            val buffer = ByteArray(size)
            inStr.read(buffer)
            inStr.close()
            return String(buffer, Charsets.UTF_8)
        }
        return ret
    }

    private fun copyAssets2InternalMem(from: String, to: String){
        if(!fileExists(to, this)){
            val fileName = to
            val jsonString : String = loadJsonFromAssets(from)
            this.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        }

    }
    private fun removeContentIMem(name: String, context: Context){
        this.openFileOutput(name, Context.MODE_PRIVATE).use {
            it.write("".toByteArray())
        }
    }

    private fun removeIMemFile(name: String, context: Context){
        context.deleteFile(name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Copy from assets to internal mem
        copyAssets2InternalMem("achievements.json", "Achievements.json")
        copyAssets2InternalMem("points.json", "Points.json")
        copyAssets2InternalMem("dailys.json", "Dailys.json")

        // load current points
        addPoints(0, this) // load points from internal mem
        updatePoints(this) // show points

        //load statistics file
        if ( !fileExists(fileName_statistics,this) ) {
            val todayDate = LocalDate.now()
            val stringDate = todayDate.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
            statistics = Statistics(stringDate,0,0,0,0,0,0)
        } else {
            statistics = loadStatisticsFromIMem(this)
        }

        // load streets internal file
        loadStreetsFromIMem(this)
        //load dailys
        dailys = loadJsonFromFile(fileName_dailys,this)
        if ( !fileExists(fileName_date_last_dailys,this) && !containsActiveDailys() ) {
            getNewDailys(this)
        } else {
            street_letter = getStreetLetter(this)
        }

        //load achievements
        achievements = loadJsonFromFile(fileName_ach,this)

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

        Log.d("Debug:", "onCreate")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(!locationEnabled()) {
            Snackbar.make(
                findViewById(R.id.map),
                "Please enable your position",
                Snackbar.LENGTH_LONG
            ).show()
        }

        loadData()
        //startTrackingPosition()
        if (backgroundEnabled){
            startLocationService()
        }
            //startLocationService()
    }

    private fun getStreetName(lat:Double, long:Double){
        var streetName:String? = null
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address : MutableList<Address> = geoCoder.getFromLocation(lat, long, 1)

        if (!address.isNullOrEmpty()) {
            streetName = address[0].thoroughfare
            if (streetName != null) {
                addStreet(streetName, this)
            }
        }
    }

    private fun drawPoint(lat:Double, long:Double) {

        val circleOptions = CircleOptions()

        circleOptions.center(LatLng(lat, long))
        circleOptions.radius(2.5)
        circleOptions.strokeColor(0xffffffff.toInt())
        circleOptions.fillColor(0xFFFF783e.toInt())
        circleOptions.strokeWidth(3f)

        mMap.addCircle(circleOptions)
    }

    private fun drawCurrentPosition(lat:Double, long:Double) {
        if (myCircle != null) {
            myCircle!!.remove()
        }

        val circleOptions = CircleOptions()

        circleOptions.center(LatLng(lat, long))
        circleOptions.radius(4.0)
        circleOptions.strokeColor(0x55FF783e)
        circleOptions.fillColor(0xFFFF401F.toInt())
        circleOptions.strokeWidth(100.0f)

        myCircle = mMap.addCircle(circleOptions)
    }

    private fun locationEnabled():Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("crumbs", null)
        val type = object : TypeToken<Array<LatLng>>() {}.getType()
        coordinates = gson.fromJson(json, type)

        if (coordinates == null) {
            coordinates = emptyArray()
        }

        backgroundEnabled = sharedPreferences.getBoolean("backgroundEnabled", true)

    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(coordinates)
        editor.putString("crumbs", json)
        editor.apply()
    }

    fun append(arr: Array<LatLng>, element: LatLng): Array<LatLng> {
        val list: MutableList<LatLng> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    private fun checkDistance(newLat:Double, newLong:Double):Boolean {
        if (coordinates.isNullOrEmpty())
            return true

        val oldPosition = coordinates!!.last()
        val result = FloatArray(1)
        Location.distanceBetween(oldPosition.latitude, oldPosition.longitude, newLat, newLong, result)

        //Log.d("Distance", result[0].toString())

        if (result[0] > 5)
            return true

        return false
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            val mark = LatLng(lastLocation.latitude, lastLocation.longitude)

            val distanceOK = checkDistance(lastLocation.latitude, lastLocation.longitude)

            if (distanceOK) {
                //coordinates = coordinates?.let { append(it, mark) }
                drawPoint(lastLocation.latitude, lastLocation.longitude)



                if (!backgroundEnabled) {
                    coordinates = coordinates?.let { append(it, mark) }
                    saveData()
                    Log.d("Background", "false")
                }
                else
                    Log.d("Background", "true")
            }

            if (firstLook){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 18f))
                firstLook = false
            }

            getStreetName(lastLocation.latitude, lastLocation.longitude)
            drawCurrentPosition(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationService() {
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.action = "startLocationService"
        startService(intent)
    }

    private fun startTrackingPosition() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10001
        //locationRequest.smallestDisplacement = 10F
        //locationRequest.numUpdates = 2

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

    private fun drawPreviousCrumbs() {
        loadData()

        for (c in coordinates!!) {
            drawPoint(c.latitude, c.longitude)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json
            )
        )

        if (!success) {
            Log.e("Debug", "Style parsing failed.")
        }

        mMap = googleMap

        drawPreviousCrumbs()

        mapLoaded = true
    }

    override fun onResume() {
        super.onResume()

        updatePoints(this)
        startTrackingPosition()

        val menuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)
        menuBtn.show()

        if (mapLoaded)
            drawPreviousCrumbs()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        firstLook = true
        stopLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        //saveData()
    }
}