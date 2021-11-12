package com.example.crumbmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OptionsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val menuFragment = MenuFragment()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    // TODO Animation ?
                    replace(R.id.FragmentContainer, menuFragment)
                    commit()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    private fun startLocationService() {
        val intent = Intent(activity?.applicationContext, LocationService::class.java)
        intent.action = "startLocationService"
        activity?.startService(intent)
    }

    private fun stopLocationService() {
        val intent = Intent(activity?.applicationContext, LocationService::class.java)
        intent.action = "stopLocationService"
        activity?.startService(intent)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("shared preferences",
            AppCompatActivity.MODE_PRIVATE
        )

        val backgroundEnabled = sharedPreferences?.getBoolean("backgroundEnabled", true)

        val switchBackgroundLocation = getView()?.findViewById<Switch>(R.id.switchBackgroundLocation)

        switchBackgroundLocation?.isChecked = backgroundEnabled == true

        switchBackgroundLocation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !backgroundEnabled!!) {
                startLocationService()
            }
            else
                stopLocationService()

            sharedPreferences?.edit()?.putBoolean("backgroundEnabled", isChecked)?.apply();
        }

    }
}