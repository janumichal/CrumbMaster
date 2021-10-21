package com.example.crumbmaster

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.beust.klaxon.Klaxon
import com.example.crumbmaster.databinding.FragmentAchievementsBinding

const val tag2 = "Debuging_TAG"

class AchievementsFragment : Fragment() {
    private var achievements : List<Achievement>? = emptyList()
    private lateinit var binding : FragmentAchievementsBinding
    private val fileName = "Achievements.json"

    private fun achObtained(ach_id: Int){
        val jsonString : String = requireContext().openFileInput(fileName).bufferedReader().readText()
        val tmpAchievements : List<Achievement>? = Klaxon().parseArray(jsonString)

        for (i in tmpAchievements!!.indices){
            if (tmpAchievements[i].id == ach_id){
                tmpAchievements[i].obtained = true
            }
        }

        val newJsonString = Klaxon().toJsonString(tmpAchievements)

        requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(newJsonString.toByteArray())
        }
        loadAchFromFile(fileName)

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
        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }

    private fun loadAchFromFile(fileName: String){
        val jsonString : String = requireContext().openFileInput("Achievements.json").bufferedReader().readText()
        achievements = Klaxon().parseArray(jsonString)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        achObtained(1) // TODO to use elesewhere
        loadAchFromFile(fileName)


        binding = FragmentAchievementsBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.ListViewAchievment.adapter = AchievementsAdapter(requireActivity(), achievements!!)

    }
}