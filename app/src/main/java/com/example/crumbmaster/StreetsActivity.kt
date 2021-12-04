package com.example.crumbmaster

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StreetsActivity : AppCompatActivity() {

    fun return2Map(){
        finish()
        overridePendingTransition(R.anim.hold, R.anim.fade_in)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streets)


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        val backBtn = findViewById<FloatingActionButton>(R.id.BackBtn_streets)
        backBtn?.setOnClickListener(){
            return2Map()
        }

        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroup)
        val inflater : LayoutInflater = layoutInflater
        val colorInt = resources.getColor(R.color.chip_color)
        val csl = ColorStateList.valueOf(colorInt)

        streetList.forEach {
            val chip = inflater.inflate(R.layout.chip, chipGroup, false) as Chip
            chip.text = it
            chip.isClickable = false
            chip.isCheckable = false
            chip.chipBackgroundColor = csl
            chipGroup.addView(chip)
        }

    }
}