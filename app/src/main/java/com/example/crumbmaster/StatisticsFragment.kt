package com.example.crumbmaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
}