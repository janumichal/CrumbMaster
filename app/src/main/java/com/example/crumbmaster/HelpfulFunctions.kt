package com.example.crumbmaster

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.beust.klaxon.Klaxon
import java.io.File

var points: Int = 0
var streetList: MutableList<String> = ArrayList()
val fileName_streets = "Streets.txt"

private fun fileExists(fname: String?, context: Context): Boolean {
    val file: File = context.getFileStreamPath(fname)
    return file.exists()
}

fun loadStreetsFromIMem(context: Context){
    if(fileExists(fileName_streets, context)){
        val tmpList: MutableList<String> = ArrayList()
        val file: File = context.getFileStreamPath(fileName_streets)
        file.forEachLine {
            tmpList.add(it)
        }
        streetList = tmpList
    }
}

private fun addStreet2IMem(name: String, context: Context){
    if(!fileExists(fileName_streets, context)){
        context.openFileOutput(fileName_streets, Context.MODE_PRIVATE).use {
            it.write("".toByteArray())
        }
    }
    val file: File = context.getFileStreamPath(fileName_streets)
    file.appendText(name + "\n")
}

private fun checkNumOfStrDiscovered(num: Int, context: Context){
    when (num) {
        1 -> {
            val id = 0
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
                Log.d(tag, "Yeey achi id: $id unlocked") // TODO delete
            }
        }
        10 -> {
            val id = 1
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        20 -> {
            val id = 2
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        30 -> {
            val id = 3
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        40 -> {
            val id = 4
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        50 -> {
            val id = 5
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        60 -> {
            val id = 6
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        70 -> {
            val id = 7
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        80 -> {
            val id = 8
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        90 -> {
            val id = 9
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        100 -> {
            val id = 10
            if(!isAchievObtainedById(id, context)){
                updateAchivById(id, context)
            }
        }
        else -> {

        }
    }
}

fun addStreet(name: String, context: Context){
    if(!streetList.contains(name)){
        streetList.add(name)
        addStreet2IMem(name, context)
        addPoints(5, context)
        checkNumOfStrDiscovered(streetList.size, context)
    }
}

//##################################################################################################
var achievements : List<Achievement>? = emptyList()
val fileName_ach = "Achievements.json"
val fileName_points = "Points.json"

fun loadJsonFromFile(fileName: String, context: Context) : List<Achievement>?{
    val jsonString : String = context.openFileInput("Achievements.json").bufferedReader().readText()
    return Klaxon().parseArray(jsonString)
}

fun addPoints(add: Int, context: Context){
    val jsonString : String = context.openFileInput(fileName_points).bufferedReader().readText()
    val tmpPoints : Points? = Klaxon().parse<Points>(jsonString)

    tmpPoints!!.points = tmpPoints.points + add;
    points = tmpPoints.points
    val newJsonString = Klaxon().toJsonString(tmpPoints)

    context.openFileOutput(fileName_points, Context.MODE_PRIVATE).use {
        it.write(newJsonString.toByteArray())
    }
}

fun updatePoints(context: Activity){
    val points_window = context.findViewById<TextView>(R.id.map_points_text)
    points_window.text = points.toString()
}

fun updateAchivById(ach_id: Int, context: Context){
    val jsonString : String = context.openFileInput(fileName_ach).bufferedReader().readText()
    val tmpAchievements : List<Achievement>? = Klaxon().parseArray(jsonString)

    for (i in tmpAchievements!!.indices){
        if (tmpAchievements[i].id == ach_id){
            tmpAchievements[i].obtained = true
            addPoints(tmpAchievements[i].points, context)
        }
    }

    val newJsonString = Klaxon().toJsonString(tmpAchievements)

    context.openFileOutput(fileName_ach, Context.MODE_PRIVATE).use {
        it.write(newJsonString.toByteArray())
    }
    // load achievements
    achievements = loadJsonFromFile(fileName_ach, context)

}

fun isAchievObtainedById(id: Int, context: Context): Boolean{
    val jsonString : String = context.openFileInput(fileName_ach).bufferedReader().readText()
    val tmpAchievements : List<Achievement>? = Klaxon().parseArray(jsonString)

    for (i in tmpAchievements!!.indices){
        if (tmpAchievements[i].id == id){
            return tmpAchievements[i].obtained
        }
    }
    return true
}