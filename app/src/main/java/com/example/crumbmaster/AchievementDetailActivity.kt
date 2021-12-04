package com.example.crumbmaster

import android.R.attr
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.example.crumbmaster.databinding.ActivityAchievementDetailBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.R.attr.button

import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable

import android.R.string.no




class AchievementDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAchievementDetailBinding


    fun return2Map(){
        finish()
        overridePendingTransition(R.anim.hold, R.anim.fade_in)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val backBtn = findViewById<FloatingActionButton>(R.id.BackBtn_detail)
        backBtn?.setOnClickListener(){
            return2Map()
        }


        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return2Map()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        val padding_in_dp = 8 // 6 dps
        val scale = resources.displayMetrics.density
        val padding_in_px = (padding_in_dp * scale + 0.5f).toInt()

        val intent = this.intent
        if(intent != null){
            val title: String = intent.getStringExtra("title")!!
            val points: String = intent.getStringExtra("points")!!
            val desc: String = intent.getStringExtra("desc")!!
            val obt: Boolean = intent.getStringExtra("obt")!!.contains("true")

            if(obt){
                binding.itemDetailBg.setBackgroundColor(Color.parseColor("#FF401F"))
                binding.itemDetailIcon.setImageResource(R.drawable.menu_icon_achievement_item_obtained)
                binding.itemDetailIcon.background = getDrawable(R.drawable.achievement_item_icon_background_obtained)
                binding.itemDetailTitle.text = title
                binding.itemDetailPoints.text = "$points Points"
                binding.itemDetailPoints.setTextColor(Color.BLACK)
                binding.pointsDetailBg.background = getDrawable(R.drawable.achievement_item_points_background_obtained)
                binding.itemDetailDesc.text = desc
                binding.itemDetailDesc.setTextColor(Color.BLACK)
                binding.itemDetailDesc.background = getDrawable(R.drawable.achievement_item_detail_background_obtained)
                binding.itemDetailDesc.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px)
                binding.pointsAchievementCheck.setImageResource(R.drawable.menu_icon_achievement_check)
            }else{
                binding.itemDetailBg.setBackgroundColor(Color.parseColor("#453C68"))
                binding.itemDetailIcon.setImageResource(R.drawable.menu_icon_achievement_item)
                binding.itemDetailIcon.background = getDrawable(R.drawable.achievement_item_icon_background)
                binding.itemDetailTitle.text = title
                binding.itemDetailPoints.text = "$points Points"
                binding.itemDetailPoints.setTextColor(Color.WHITE)
                binding.pointsDetailBg.background = getDrawable(R.drawable.achievement_item_points_background)
                binding.itemDetailDesc.text = desc
                binding.itemDetailDesc.setTextColor(Color.WHITE)
                binding.itemDetailDesc.background = getDrawable(R.drawable.achievement_item_detail_background)
                binding.itemDetailDesc.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px)
            }

        }


    }
}