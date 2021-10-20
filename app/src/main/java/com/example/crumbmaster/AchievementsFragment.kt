package com.example.crumbmaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import java.io.IOException
import com.beust.klaxon.Klaxon
import com.example.crumbmaster.databinding.AchievmentListItemBinding
import com.example.crumbmaster.databinding.FragmentAchievementsBinding

class AchievementsFragment : Fragment() {
    private var achievements : List<Achievement>? = emptyList()
    private lateinit var binding : FragmentAchievementsBinding


    @Throws(IOException::class)
    private fun loadJsonFromAssets(file : String): String{
        val inStr = activity?.assets?.open(file)
        val ret = ""
        if (inStr != null){
            val size = inStr.available()
            val buffer = ByteArray(size)
            inStr.read(buffer)
            inStr.close()
            return String(buffer, Charsets.UTF_8)
        }
        return ret
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val jsonString : String = loadJsonFromAssets("achievements.json")
        achievements = Klaxon().parseArray(jsonString)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAchievementsBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.ListViewAchievment.adapter = AchievementsAdapter(requireActivity(), achievements!!)

    }
}