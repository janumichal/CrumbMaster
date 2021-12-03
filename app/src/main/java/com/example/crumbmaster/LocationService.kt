package com.example.crumbmaster

import android.Manifest
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import android.app.NotificationManager

import android.app.NotificationChannel

import android.os.Build

import android.R
import android.annotation.SuppressLint

import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.gson.reflect.TypeToken
import java.util.*


class LocationService : Service() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    var coordinates: Array<LatLng>? = emptyArray()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
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
//        streetName = address[0].thoroughfare
//        addStreet(streetName, this)
        /**if(streetName != null)
        Log.d("Debug:", "Ulica $streetName")*/
    }

    private fun checkDistance(newLat:Double, newLong:Double):Boolean {
        if (coordinates.isNullOrEmpty())
            return true

        val oldPosition = coordinates!!.last()
        val result = FloatArray(1)
        Location.distanceBetween(oldPosition.latitude, oldPosition.longitude, newLat, newLong, result)

        Log.d("Distance", result[0].toString())

        if (result[0] > 5)
            return true

        return false
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(coordinates)
        editor.putString("crumbs", json)
        editor.apply()
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

    }

    fun append(arr: Array<LatLng>, element: LatLng): Array<LatLng> {
        val list: MutableList<LatLng> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            val mark = LatLng(lastLocation.latitude, lastLocation.longitude)

            val distanceOK = checkDistance(lastLocation.latitude, lastLocation.longitude)

            if (distanceOK) {
                coordinates = coordinates?.let { append(it, mark) }
                saveData()
            }

            Log.d("Debug:", "Location: ${lastLocation.latitude}, ${lastLocation.longitude}")

            getStreetName(lastLocation.latitude, lastLocation.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationService() {
        val channelId = "location_notification_channel"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
        //builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Location Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Location Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "Channel for location service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        loadData()

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10001
        //locationRequest.smallestDisplacement = 10F
        //locationRequest.numUpdates = 2
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )

        startForeground(175, builder.build())
    }

    private fun stopLocationService() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == "startLocationService") {
                    startLocationService()
                } else if (action == "stopLocationService") {
                    stopLocationService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


}