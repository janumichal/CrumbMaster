package com.example.crumbmaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StatisticsFragment : Fragment() {

    private fun swap2Fragment(fragment : Fragment){
        requireActivity().supportFragmentManager.beginTransaction().apply {
            //TODO Animation here
            replace(R.id.FragmentContainer, fragment)
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val menuFragment = MenuFragment()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    // TODO Animation ?
                    replace(R.id.FragmentContainer, menuFragment)
                    commit()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuFragment = MenuFragment()
        val backBtn = view?.findViewById<FloatingActionButton>(R.id.BackBtn_stats)
        backBtn?.setOnClickListener(){
            swap2Fragment(menuFragment)
        }

        //update stats
        val created = view.findViewById<TextView>(R.id.created_value)
        val crumbsTotal = view.findViewById<TextView>(R.id.drawn_crumbs_value)
        val crumbsMaxPerDay = view.findViewById<TextView>(R.id.drawn_crumbs_per_day_value)
        val crumbsToday = view.findViewById<TextView>(R.id.drawn_crumbs_today_value)
        val questsTotal = view.findViewById<TextView>(R.id.quest_value)
        val achievements = view.findViewById<TextView>(R.id.achievement_value)
        val pointsTotal = view.findViewById<TextView>(R.id.point_value)
        val streetsTotal = view.findViewById<TextView>(R.id.street_value)
        val streetsMaxPerDay = view.findViewById<TextView>(R.id.street_per_day_value)
        val streetsToday = view.findViewById<TextView>(R.id.street_today_value)

        created.text = statistics?.started
        crumbsTotal.text = statistics?.crumbs_total.toString()
        crumbsMaxPerDay.text = statistics?.max_crumbs_per_day.toString()
        crumbsToday.text = statistics?.crumbs_today.toString()
        questsTotal.text = statistics?.quests_total.toString()
        val textAchievement = achievementsObtained().toString() + "/11"
        achievements.text = textAchievement
        pointsTotal.text = points.toString()
        streetsTotal.text = streetList.size.toString()
        streetsMaxPerDay.text = statistics?.max_streets_per_day.toString()
        streetsToday.text = statistics?.streets_today.toString()
    }
}