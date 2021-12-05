package com.example.crumbmaster

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MenuFragment : Fragment() {

    fun return2Map(){
        activity?.finish()
        activity?.overridePendingTransition(R.anim.hold, R.anim.fade_in)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return2Map()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    private fun swap2Fragment(fragment : Fragment){
        requireActivity().supportFragmentManager.beginTransaction().apply {
            //TODO Animation here
            replace(R.id.FragmentContainer, fragment)
            commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // BACK
        val backBtn = getView()?.findViewById<FloatingActionButton>(R.id.BackBtn)
        backBtn?.setOnClickListener(){
            return2Map()
        }

        //OPTIONS
        val optionsBtn = getView()?.findViewById<Button>(R.id.SettingsBtn)
        optionsBtn?.setOnClickListener(){
            val optionsFragment = OptionsFragment()
            swap2Fragment(optionsFragment)
        }

        // ACHIEVEMENTS
        val achievementsBtn = getView()?.findViewById<Button>(R.id.AchievementsBtn)
        achievementsBtn?.setOnClickListener(){
//            val achievementsFragment = AchievementsFragment()
//            swap2Fragment(achievementsFragment)
            val intent = Intent(context, AchievemtsListActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.hold, R.anim.hold)
        }

        //DAILYS
        val dailyQuestsBtn = getView()?.findViewById<Button>(R.id.dailyQuest)
        dailyQuestsBtn?.setOnClickListener(){
            val intent = Intent(context, DailysListActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.hold, R.anim.hold)
        }


        // STATISTICS
        val statisticsBtn = getView()?.findViewById<Button>(R.id.StatisticsBtn)
        statisticsBtn?.setOnClickListener(){
            val statisticsFragment = StatisticsFragment()
            swap2Fragment(statisticsFragment)
        }

        // ABOUT
        val aboutBtn = getView()?.findViewById<Button>(R.id.about)
        aboutBtn?.setOnClickListener(){
            val intent = Intent(context, AboutActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.hold, R.anim.hold)
        }

        //ROADS
        val streetsBtn = getView()?.findViewById<Button>(R.id.dicsStreets)
        streetsBtn?.setOnClickListener(){
            val intent = Intent(context, StreetsActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.hold, R.anim.hold)
        }

    }

}