package com.example.crumbmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mMenuBtn = findViewById<FloatingActionButton>(R.id.MenuBtn);
//        val mMenuLayout = findViewById<RelativeLayout>(R.id.MenuLayout);

        mMenuBtn.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent);
            overridePendingTransition(R.anim.hold, R.anim.fade_in)

        }

    }
}