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

private var achievements : List<Achievement>? = emptyList()
private val fileName_ach = "Achievements.json"
private val fileName_points = "Points.json"

public fun loadJsonFromFile(fileName: String, context: Context) : List<Achievement>?{
    val jsonString : String = context.openFileInput("Achievements.json").bufferedReader().readText()
    return Klaxon().parseArray(jsonString)
}

fun updatePointsAdd(add: Int, context: Context){
    val jsonString : String = context.openFileInput(fileName_points).bufferedReader().readText()
    val tmpPoints : Points? = Klaxon().parse<Points>(jsonString)

    tmpPoints!!.points = tmpPoints.points + add;
    points = tmpPoints.points
    val newJsonString = Klaxon().toJsonString(tmpPoints)

    context.openFileOutput(fileName_points, Context.MODE_PRIVATE).use {
        it.write(newJsonString.toByteArray())
    }
}

fun updatePointsWindow(context: Activity){
    val points_window = context.findViewById<TextView>(R.id.map_points_text)
    points_window.text = points.toString()
}

fun updateAchivById(ach_id: Int, context: Context){
    val jsonString : String = context.openFileInput(fileName_ach).bufferedReader().readText()
    val tmpAchievements : List<Achievement>? = Klaxon().parseArray(jsonString)

    for (i in tmpAchievements!!.indices){
        if (tmpAchievements[i].id == ach_id){
            tmpAchievements[i].obtained = true
            updatePointsAdd(tmpAchievements[i].points, context)
        }
    }

    val newJsonString = Klaxon().toJsonString(tmpAchievements)

    context.openFileOutput(fileName_ach, Context.MODE_PRIVATE).use {
        it.write(newJsonString.toByteArray())
    }
    // load achievements
    achievements = loadJsonFromFile(fileName_ach, context)

}

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