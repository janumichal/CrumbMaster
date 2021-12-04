package com.example.crumbmaster

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class DailyAdapter (context: Activity, private val list: List<Daily>, ) : ArrayAdapter<Daily>(context, R.layout.dailys_list_item, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.dailys_list_item, null)

        val title = view.findViewById<TextView>(R.id.item_title)
        val points = view.findViewById<TextView>(R.id.item_progress)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_quest_bar)
        val icon = view.findViewById<ImageView>(R.id.item_icon)

        if ( !list[position].active || list[position].goal <= list[position].progress ) {
            title.text = list[position].title
            progressBar.visibility = View.GONE;
            val questLayout = view.findViewById<View>(R.id.item_background)
            questLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.achievement_item_background_obtained))
            points.text = "Congratulations you earned " + list[position].points + " points !"
            icon.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.achievement_item_icon_background_obtained))
            icon.setImageResource(R.drawable.menu_icon_achievement_item_obtained)
        } else {
            title.text = list[position].title
            val score = list[position].progress.toString() + " / " + list[position].goal.toString()
            points.text = score
            progressBar.max = list[position].goal
            progressBar.progress = list[position].progress
        }
        return view
    }
}