package com.example.crumbmaster

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.beust.klaxon.Klaxon
import java.io.File
import java.io.IOException


const val tag = "Debuging_TAG" // TODO remove later


class MapActivity : AppCompatActivity() {

    private fun scaleCircleAnim(context: Context){
        val menuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)
        menuBtn.hide()
        val displayMatrics = DisplayMetrics()
        // from android api 30
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            display?.getRealMetrics(displayMatrics)
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMatrics)
        }
        val dHeight = displayMatrics.heightPixels
        val circle = findViewById<ImageView>(R.id.MenuCircle)
        val cHeight = circle.height
        val scaleVariable = dHeight/(cHeight/2) + 1

        val scaleY = ObjectAnimator.ofFloat(circle, "ScaleY", scaleVariable.toFloat())
        val scaleX = ObjectAnimator.ofFloat(circle, "ScaleX", scaleVariable.toFloat())
        scaleX.duration = 600
        scaleY.duration = 600

        val scaleUp = AnimatorSet()

        scaleUp.play(scaleX).with(scaleY)
        scaleUp.start()
        scaleUp.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?){}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                val intent = Intent(context, MenuActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_out, R.anim.hold)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        recreate()
    }

    @Throws(IOException::class)
    private fun loadJsonFromAssets(file : String): String{
        val inStr = this.assets?.open(file)
        val ret = ""
        if (inStr != null){
            val size = inStr.available()
            val buffer = ByteArray(size)
            inStr.read(buffer)
            inStr.close()
            return String(buffer, Charsets.UTF_8)
        }
        return ret
    }

    fun fileExists(fname: String?): Boolean {
        val file: File = baseContext.getFileStreamPath(fname)
        return file.exists()
    }

    private fun copyAchievements(){
        if(!fileExists("Achievements.json")){
            val fileName = "Achievements.json"
            val jsonString : String = loadJsonFromAssets("achievements.json")
            this.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
            Log.d(tag, "File Created")
        }else{
            // TODO DELETE
            Log.d(tag, "File Exists")
//            val fileName = "Achievements.json"
//            val jsonString : String = loadJsonFromAssets("achievements.json")
//            this.openFileOutput(fileName, Context.MODE_PRIVATE).use {
//                it.write(jsonString.toByteArray())
//            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        copyAchievements()

        val mMenuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)
        mMenuBtn.setOnClickListener{
            scaleCircleAnim(this)
        }

    }
}