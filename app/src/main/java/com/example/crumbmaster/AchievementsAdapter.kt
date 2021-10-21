package com.example.crumbmaster

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AchievementsAdapter (context: Activity, private val list: List<Achievement>, ) : ArrayAdapter<Achievement>(context, R.layout.achievment_list_item, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.achievment_list_item, null)

        val icon = view.findViewById<ImageView>(R.id.item_icon)
        val title = view.findViewById<TextView>(R.id.item_title)
        val desc = view.findViewById<TextView>(R.id.item_description)
        val points = view.findViewById<TextView>(R.id.item_points)

        // TODO ICONS LOAD
        if (list[position].obtained){
            icon.setImageResource(R.drawable.obt)
        }

        Log.d(tag, "title: ${list[position].title}, dest: ${list[position].description}, points: ${list[position].points.toString()}")

        title.text = list[position].title
        desc.text = list[position].description
        points.text = list[position].points.toString()

        return view
    }
}