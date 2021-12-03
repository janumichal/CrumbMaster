package com.example.crumbmaster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.crumbmaster.databinding.ActivityDailysListBinding
import android.os.CountDownTimer
import android.text.Layout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.google.android.gms.maps.SupportMapFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailysListActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDailysListBinding
    private var time_in_milli_seconds = 0L
    private lateinit var countdown_timer: CountDownTimer

    private fun startTimer() {
        val current = LocalDateTime.now()
        val hours = current.format(DateTimeFormatter.ofPattern("HH")).toLong()
        val minutes = current.format(DateTimeFormatter.ofPattern("mm")).toLong()
        val seconds = current.format(DateTimeFormatter.ofPattern("ss")).toLong()

        var miliseconds = (23 - hours) * 3600000L +
                (59 - minutes) * 60000L +
                (59 - seconds) * 1000L

        this.countdown_timer = object : CountDownTimer(miliseconds, 1000) {
            override fun onFinish() {
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                val hours = ((time_in_milli_seconds / 1000) / 60) / 60
                val minutes = ((time_in_milli_seconds / 1000) / 60 ) % 60
                val seconds = (time_in_milli_seconds / 1000) % 60

                val hoursText = if (hours < 10) ("0$hours") else hours.toString()
                val minutesText = if (minutes < 10) ("0$minutes" ) else minutes.toString()
                val secondsText = if (seconds < 10) ("0$seconds" ) else seconds.toString()

                val stringTime = "next dailys $hoursText:$minutesText:$secondsText"

                val dailys_count_down = findViewById<TextView>(R.id.dailys_count_down)
                dailys_count_down.text = stringTime
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

        startTimer()

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
