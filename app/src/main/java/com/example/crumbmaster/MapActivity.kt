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
import android.util.Log
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val tag = "Debuging_TAG" // TODO remove later


class MapActivity : AppCompatActivity() {
    private fun getScaleAnimation(){
        val a = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        a.reset()
        val cl = findViewById<ConstraintLayout>(R.id.MapLayout)
        cl.clearAnimation()
        cl.startAnimation(a)
    }

    private fun scaleCircleAnim(context: Context){
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
        val scaleVariable = dHeight/(cHeight/2 - cHeight/4)

        Log.d(tag, "Display height: $dHeight")
        Log.d(tag, "Circle height: $cHeight")
        Log.d(tag, "Multiplier: $scaleVariable")

        val scaleY = ObjectAnimator.ofFloat(circle, "ScaleY", scaleVariable.toFloat())
        val scaleX = ObjectAnimator.ofFloat(circle, "ScaleX", scaleVariable.toFloat())
        
        scaleX.duration = 1000
        scaleY.duration = 1000

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mMenuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn)

        mMenuBtn.setOnClickListener{


            scaleCircleAnim(this)

//            val intent = Intent(this,MenuActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.fade_out, R.anim.hold)

        }

    }
}