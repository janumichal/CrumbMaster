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
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OptionsFragment : Fragment() {
    private fun swap2Fragment(fragment : Fragment){
        requireActivity().supportFragmentManager.beginTransaction().apply {
            //TODO Animation here
            replace(R.id.FragmentContainer, fragment)
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val menuFragment = MenuFragment()
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
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

        val menuFragment = MenuFragment()
        val backBtn = view?.findViewById<FloatingActionButton>(R.id.BackBtn_settings)
        backBtn?.setOnClickListener(){
            swap2Fragment(menuFragment)
        }

        val sharedPreferences = activity?.getSharedPreferences("shared preferences",
            AppCompatActivity.MODE_PRIVATE
        )

        val backgroundEnabled = sharedPreferences?.getBoolean("backgroundEnabled", true)

        val switchBackgroundLocation = getView()?.findViewById<SwitchCompat>(R.id.switchBackgroundLocation)

        switchBackgroundLocation?.isChecked = backgroundEnabled == true

        switchBackgroundLocation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !backgroundEnabled!!) {
                startLocationService()
            }
            else if (isChecked) {
                startLocationService()
            }
            else
                stopLocationService()

            sharedPreferences?.edit()?.putBoolean("backgroundEnabled", isChecked)?.apply();
        }

    }
}