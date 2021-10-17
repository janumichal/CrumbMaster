package com.example.crumbmaster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback

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

    fun swap2Fragment(fragment : Fragment){
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            //TODO Animation here
            replace(R.id.FragmentContainer, fragment)
            commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // BACK
        val backBtn = getView()?.findViewById<Button>(R.id.BackBtn)
        backBtn?.setOnClickListener(){
            return2Map()
        }

        //OPTIONS
        val optionsBtn = getView()?.findViewById<Button>(R.id.OptionsBtn)
        optionsBtn?.setOnClickListener(){
            val optionsFragment = OptionsFragment()
            swap2Fragment(optionsFragment)
        }

        // ACHIEVEMENTS
        val achievementsBtn = getView()?.findViewById<Button>(R.id.AchievementsBtn)
        achievementsBtn?.setOnClickListener(){
            val achievementsFragment = AchievementsFragment()
            swap2Fragment(achievementsFragment)
        }


        // STATISTICS
        val statisticsBtn = getView()?.findViewById<Button>(R.id.StatisticsBtn)
        statisticsBtn?.setOnClickListener(){
            val statisticsFragment = StatisticsFragment()
            swap2Fragment(statisticsFragment)
        }

    }

}