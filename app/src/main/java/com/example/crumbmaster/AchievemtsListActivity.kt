package com.example.crumbmaster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.beust.klaxon.Klaxon
import com.example.crumbmaster.databinding.ActivityAchievemtsListBinding

class AchievemtsListActivity : AppCompatActivity() {
    private var achievements : List<Achievement>? = emptyList()
    private lateinit var binding : ActivityAchievemtsListBinding
    private val fileName = "Achievements.json"

    private fun loadAchFromFile(fileName: String){
        val jsonString : String = openFileInput("Achievements.json").bufferedReader().readText()
        achievements = Klaxon().parseArray(jsonString)
    }

    private fun achObtained(ach_id: Int){
        val jsonString : String = openFileInput(fileName).bufferedReader().readText()
        val tmpAchievements : List<Achievement>? = Klaxon().parseArray(jsonString)

        for (i in tmpAchievements!!.indices){
            if (tmpAchievements[i].id == ach_id){
                tmpAchievements[i].obtained = true
            }
        }

        val newJsonString = Klaxon().toJsonString(tmpAchievements)

        openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(newJsonString.toByteArray())
        }
        loadAchFromFile(fileName)

    }


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


        achObtained(1) // TODO to use elesewhere
        loadAchFromFile(fileName)
        binding.ListViewAchievment.adapter = AchievementsAdapter(this, achievements!!)

    }





}