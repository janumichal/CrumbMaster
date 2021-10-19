package com.example.crumbmaster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import java.io.IOException
import com.beust.klaxon.Klaxon

const val tag2 : String= "Debuging_TAG" // TODO remove later

class AchievementsFragment : Fragment() {
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

        val jsonString : String = loadJsonFromAssets("achievements.json")
        val achievements = Klaxon().parseArray<Achievement>(jsonString)


        val lva = activity?.findViewById<ListView>(R.id.ListViewAchiev)
        val array = ArrayList<String>()

        achievements?.forEach {
            array.add(it.title)
        }

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, array)
        lva?.adapter = arrayAdapter



    }
}