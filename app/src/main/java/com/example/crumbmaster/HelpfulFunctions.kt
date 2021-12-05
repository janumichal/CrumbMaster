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
    updateDailys(1,0,context)
    updateCrumbsTotal(context)

    updateCrumbsToday(context)
    statistics?.let { updateMaxCrumbsPerDay(it.crumbs_today,context) }

    if(!streetList.contains(name)){
        updateDailys(1,1,context)
        streetList.add(name)
        addStreet2IMem(name, context)
        addPoints(5, context)
        checkNumOfStrDiscovered(streetList.size, context)

        updateStreetsToday(context)
        statistics?.let { updateMaxStreetsPerDay(it.streets_today,context) }
    }
    if (name[0] == street_letter ) {
        updateDailys(1,2,context)
    }
    writeStatisticsToIMem(context)
}

fun achievementsObtained() : Int {
    var number = 0
    achievements?.forEach {
        if (it.obtained) {
            number += 1
        }
    }
    return number
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
val fileName_statistics = "Statistics.json"
var street_letter = Char.MIN_VALUE
var statistics : Statistics? = null

inline fun <reified T> loadJsonFromFile(fileName: String, context: Context) : List<T>?{
    val jsonString : String = context.openFileInput(fileName).bufferedReader().readText()
    return Klaxon().parseArray(jsonString)
}

fun loadStatisticsFromIMem(context: Context) : Statistics? {
    val jsonString : String = context.openFileInput(fileName_statistics).bufferedReader().readText()
    return Klaxon().parse(jsonString)
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
        }
        if (i == 2) {
            if (daily != null) {
                street_letter = daily.title[daily.title.length-1]
            }
        }
    }

    writeDailysToFile(context)
    return dailys
}

fun getActiveDailys() : List<Daily>? {
    val newDaily = arrayListOf<Daily?>()
    dailys?.forEach {
        if ( it.active ) {
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
            it.obtained = false
        }
    }

    statistics!!.crumbs_today = 0
    statistics!!.streets_today = 0
}

fun updateDailys(progress: Int,type: Int,context: Context) {
    dailys?.forEach {
        if (it.active && it.type == type) {
            it.progress += progress
            if ( it.goal <= it.progress && !it.obtained ) {
                it.obtained = true
                addPoints(it.points,context)
                updateQuestsTotal(context)
            }
        }
    }
    writeDailysToFile(context)
    writeStatisticsToIMem(context)
}

fun getStreetLetter(context: Context) : Char {
    dailys?.forEach {
        if (it.active && it.type == 2 ) {
             return it.title[it.title.length-1]
        }
    }
    return Char.MIN_VALUE
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

fun updateCrumbsTotal(context: Context ) {
    statistics?.crumbs_total = statistics?.crumbs_total?.plus(1)!!
}

fun updateQuestsTotal(context: Context ) {
    statistics?.quests_total = statistics?.quests_total?.plus(1)!!
}

fun updateCrumbsToday(context: Context) {
    statistics?.crumbs_today = statistics?.crumbs_today?.plus(1)!!
}

fun updateStreetsToday(context: Context) {
    statistics?.streets_today = statistics?.streets_today?.plus(1)!!
}

fun updateMaxCrumbsPerDay( value: Int,context: Context ) {
    val max_crumbs = statistics?.max_crumbs_per_day
    if (max_crumbs != null) {
        if ( max_crumbs <= value ) {
            statistics?.max_crumbs_per_day = value
        }
    }
}

fun updateMaxStreetsPerDay( value: Int,context: Context ) {
    val max_streets = statistics?.max_streets_per_day
    if (max_streets != null) {
        if ( max_streets <= value ) {
            statistics?.max_streets_per_day = value
        }
    }
}

fun writeStatisticsToIMem(context: Context) {
    val newJsonString = Klaxon().toJsonString(statistics)
    context.openFileOutput(fileName_statistics, Context.MODE_PRIVATE).use {
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