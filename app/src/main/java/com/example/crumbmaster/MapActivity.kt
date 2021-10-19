package com.example.crumbmaster

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton



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
                recreate()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mMenuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)
        mMenuBtn.setOnClickListener{
            scaleCircleAnim(this)
        }

    }
}