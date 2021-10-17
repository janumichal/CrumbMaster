package com.example.crumbmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val menuFragment = MenuFragment()

        // show menu fragment initially
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.FragmentContainer, menuFragment)
            commit()
        }
    }
}