package com.example.crumbmaster

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AboutActivity : AppCompatActivity() {

    fun return2Map(){
        finish()
        overridePendingTransition(R.anim.hold, R.anim.fade_in)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        val backBtn = findViewById<FloatingActionButton>(R.id.BackBtn_about)
        backBtn?.setOnClickListener(){
            return2Map()
        }

    }
}