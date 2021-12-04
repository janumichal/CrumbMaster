package com.example.crumbmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.crumbmaster.databinding.ActivityAchievemtsListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        setContentView(binding.root)

        val backBtn = findViewById<FloatingActionButton>(R.id.BackBtn_achiev)
        backBtn?.setOnClickListener(){
            return2Map()
        }


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        achievements = loadJsonFromFile(fileName_ach, this)

        binding.ListViewAchievment.adapter = AchievementsAdapter(this, achievements!!)
        binding.ListViewAchievment.isClickable = true

        binding.ListViewAchievment.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, AchievementDetailActivity::class.java)
            intent.putExtra("title", achievements!![position].title)
            intent.putExtra("points", achievements!![position].points.toString())
            intent.putExtra("desc", achievements!![position].description)
            intent.putExtra("obt", achievements!![position].obtained.toString())
            startActivity(intent)
        }
    }





}