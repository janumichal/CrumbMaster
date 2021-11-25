package com.example.crumbmaster

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.beust.klaxon.Klaxon
import com.example.crumbmaster.databinding.ActivityAchievemtsListBinding

const val tag = "Debuging_TAG" // TODO remove later




//######################################################################################################

class AchievemtsListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAchievemtsListBinding



    fun return2Map(){
        finish()
        overridePendingTransition(R.anim.hold, R.anim.fade_in)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievemtsListBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)


        updateAchivById(1, this) // TODO to use elesewhere
        achievements = loadJsonFromFile(fileName_ach, this)

        binding.ListViewAchievment.adapter = AchievementsAdapter(this, achievements!!)

    }





}