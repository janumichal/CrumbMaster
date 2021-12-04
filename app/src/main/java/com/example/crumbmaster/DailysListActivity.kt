package com.example.crumbmaster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.crumbmaster.databinding.ActivityDailysListBinding
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.beust.klaxon.Klaxon
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter

class DailysListActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDailysListBinding

    private fun startTimer(context: Context) {
        val current = LocalDateTime.now()

        val hours = current.format(DateTimeFormatter.ofPattern("HH")).toLong()
        val minutes = current.format(DateTimeFormatter.ofPattern("mm")).toLong()
        val seconds = current.format(DateTimeFormatter.ofPattern("ss")).toLong()

        lateinit var countdown_timer: CountDownTimer

        var miliseconds = (23 - hours) * 3600000L +
                (59 - minutes) * 60000L +
                (59 - seconds) * 1000L

        countdown_timer = object : CountDownTimer(miliseconds, 1000) {
            override fun onFinish() {
                getNewDailys(context)
                return2Map()
            }
            override fun onTick(p0: Long) {
                var time_in_milli_seconds = p0
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

        val backBtn = findViewById<FloatingActionButton>(R.id.BackBtn_dailys)
        backBtn?.setOnClickListener(){
            return2Map()
        }

        startTimer(this)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        if ( !fileExists(fileName_date_last_dailys,this) ) {
            dailys = getNewDailys( this)
        }
        val lastUpdateDate = getLastUpdateDailys(this)
        val lastUpdateString = lastUpdateDate.format(DateTimeFormatter.ofPattern("yyyy-MM-d"))

        val current = LocalDate.now()
        val date = current.format(DateTimeFormatter.ofPattern("yyyy-MM-d"))

        if ( lastUpdateString != date ) {
            dailys = getNewDailys( this)
        }

        val tmpDailys = getActiveDailys();
        binding.ListViewDailys.adapter = DailyAdapter(this, tmpDailys!!)
    }
}
