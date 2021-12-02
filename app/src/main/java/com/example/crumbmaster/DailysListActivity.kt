package com.example.crumbmaster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.crumbmaster.databinding.ActivityDailysListBinding
import android.os.CountDownTimer
import android.text.Layout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.google.android.gms.maps.SupportMapFragment

var START_MILLI_SECONDS = 60000L

class DailysListActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDailysListBinding
    private var time_in_milli_seconds = 0L
    private lateinit var countdown_timer: CountDownTimer

    private fun startTimer(time_in_seconds: Long) {
        this.countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                val minute = (time_in_milli_seconds / 1000) / 60
                val seconds = (time_in_milli_seconds / 1000) % 60
            }
        }
        countdown_timer.start()
    }

    fun return2Map() {
        finish()
        overridePendingTransition(R.anim.hold, R.anim.fade_in)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailysListBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        dailys = loadJsonFromFile(fileName_dailys, this)
        binding.ListViewDailys.adapter = DailyAdapter(this, dailys!!)
    }

}
