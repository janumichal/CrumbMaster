package com.example.crumbmaster

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.beust.klaxon.Klaxon
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var points: Int = 0
var streetList: MutableList<String> = ArrayList()
val fileName_streets = "Streets.txt"
val fileName_date_last_dailys = "DateLastUpdate.txt"

fun fileExists(fname: String?, context: Context): Boolean {
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
        updateDailys(1,1,context)
        streetList.add(name)
        addStreet2IMem(name, context)
        addPoints(5, context)
        checkNumOfStrDiscovered(streetList.size, context)
    }
}

fun importLastUpdateDailys2Mem(context: Context,date: String) {
    if(!fileExists(fileName_date_last_dailys, context)){
        context.openFileOutput(fileName_date_last_dailys, Context.MODE_PRIVATE).use {
            it.write(date.toByteArray())
        }
    } else {
        context.openFileOutput(fileName_date_last_dailys, Context.MODE_PRIVATE).use {
            it.write(date.toByteArray())
        }
    }

    Log.d("Import Mem", "Import new date to Mem" + date)
    val newDate = getLastUpdateDailys(context)
    Log.d("Import Mem", "Import new date to Mem" + newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

}

fun getLastUpdateDailys(context: Context): LocalDate {
    val stringDate: String = context.openFileInput(fileName_date_last_dailys).bufferedReader().readText()
    return LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE)
}

//##################################################################################################
var achievements : List<Achievement>? = emptyList()
var dailys : List<Daily>? = emptyList()
val fileName_ach = "Achievements.json"
val fileName_points = "Points.json"
val fileName_dailys = "Dailys.json"


inline fun <reified T> loadJsonFromFile(fileName: String, context: Context) : List<T>?{
    val jsonString : String = context.openFileInput(fileName).bufferedReader().readText()
    return Klaxon().parseArray(jsonString)
}

fun getNewDailys(context: Context) : List<Daily>? {
    resetDailys()
    writeDailysToFile(context)
    val current = LocalDateTime.now()
    val date = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    importLastUpdateDailys2Mem(context,date)

    dailys = loadJsonFromFile(fileName_dailys, context)
    val newJsonString = Klaxon().toJsonString(dailys)
    Log.d("List Dailys", "getNewDailys: " + newJsonString)

    for ( i in 0..2 ) {
        val questId = (0..2).random() + i * 3
        val daily : Daily? = dailys?.get(questId)
        if (daily != null) {
            daily.active = true
        };
    }

    writeDailysToFile(context)
    return dailys
}

fun getActiveOrInProgressDailys() : List<Daily>? {
    val newDaily = arrayListOf<Daily?>()
    dailys?.forEach {
        if ( it.active && it.progress < it.goal ) {
            newDaily.add(it);
        }
    }
    return newDaily as List<Daily>
}

fun resetDailys() {
    dailys?.forEach {
        if (it.active) {
            it.active = false
            it.progress = 0
        }
    }
    val newJsonString = Klaxon().toJsonString(dailys)
    Log.d("RESET", "reseted values: " + newJsonString)
}

fun updateDailys(progress: Int,type: Int,context: Context) {
    dailys?.forEach {
        if (it.active && it.type == type) {
            it.progress += progress
        }
    }
    writeDailysToFile(context)
}

fun containsActiveDailys() : Boolean {
    dailys?.forEach {
        if (it.active ) {
            return true
        }
    }
    return false
}

fun writeDailysToFile(context: Context) {
    val newJsonString = Klaxon().toJsonString(dailys)
    context.openFileOutput(fileName_dailys, Context.MODE_PRIVATE).use {
        it.write(newJsonString.toByteArray())
    }
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