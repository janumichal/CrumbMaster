package com.example.crumbmaster

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class AchievementsAdapter (context: Activity, private val list: List<Achievement>, ) : ArrayAdapter<Achievement>(context, R.layout.achievment_list_item, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.achievment_list_item, null)

        val icon = view.findViewById<ImageView>(R.id.item_icon)
        val title = view.findViewById<TextView>(R.id.item_title)
        val background = view.findViewById<RelativeLayout>(R.id.item_background)
//        val desc = view.findViewById<TextView>(R.id.item_description)
        val points = view.findViewById<TextView>(R.id.item_points)
        val points_bg = view.findViewById<LinearLayout>(R.id.points_bg)
        val check = view.findViewById<ImageView>(R.id.achievement_check)

        if (list[position].obtained){
            background.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.achievement_item_background_obtained))
            icon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.achievement_item_icon_background_obtained))
            icon.setImageResource(R.drawable.menu_icon_achievement_item_obtained)
            points_bg.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.achievement_item_points_background_obtained))
            points.setTextColor(Color.parseColor("#000000"))
            check.setImageResource(R.drawable.menu_icon_achievement_check)
        }

        title.text = list[position].title
//        desc.text = list[position].description
        points.text = list[position].points.toString()

        return view
    }
}