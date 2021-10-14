package com.example.crumbmaster

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val tag = "Debuging_TAG" // TODO remove later

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mMenuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)

        mMenuBtn.setOnClickListener{
            val displayMatrics = DisplayMetrics()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                @Suppress("DEPRECATION")
                display?.getRealMetrics(displayMatrics)
            } else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay.getMetrics(displayMatrics)
            }
            val dHeight = displayMatrics.heightPixels

            Log.d(tag, dHeight.toString())



            val circle = findViewById<ImageView>(R.id.MenuCircle)
            val cHeight = circle.height
            Log.d(tag, "Circle height: $cHeight")

            val scaleVariable = dHeight/(cHeight/2 - cHeight/4)
            Log.d(tag, "Multiplier: $scaleVariable")

            val scaleY = ObjectAnimator.ofFloat(circle, "ScaleY", scaleVariable.toFloat())
            val scaleX = ObjectAnimator.ofFloat(circle, "ScaleX", scaleVariable.toFloat())

            scaleX.duration = 1000
            scaleY.duration = 1000

            val scaleUp = AnimatorSet()

            scaleUp.play(scaleX).with(scaleY)
            scaleUp.start()

//            val intent = Intent(this,MenuActivity::class.java)
//            startActivity(intent);
//            overridePendingTransition(R.anim.hold, R.anim.fade_in)

        }

    }
}