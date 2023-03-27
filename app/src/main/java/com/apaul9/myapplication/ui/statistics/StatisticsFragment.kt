package com.apaul9.myapplication.ui.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.navGraphViewModels
import com.apaul9.myapplication.R
import com.apaul9.myapplication.ui.playgame.PlayGameViewModel


class StatisticsFragment : Fragment() {

    private val viewModel: PlayGameViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var totalGame: TextView
    private lateinit var winCount: TextView
    private lateinit var loseCount: TextView
    private lateinit var quickTime: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Initialize the lateinit var here
        totalGame = view.findViewById(R.id.gamesPlayed_textView)
        winCount = view.findViewById(R.id.gamesWon_textView)
        loseCount = view.findViewById(R.id.gamesLost_textView)
        quickTime = view.findViewById(R.id.fastest_time_textView)

        viewModel.gamesPlayed.observe(viewLifecycleOwner) { gamesPlayed ->
            totalGame.text = gamesPlayed.toString()
        }

        viewModel.gamesWon.observe(viewLifecycleOwner) { gamesWon ->
            winCount.text = gamesWon.toString()
        }

        viewModel.gamesLost.observe(viewLifecycleOwner) { gamesLost ->
            loseCount.text = gamesLost.toString()
        }




        return view

    }

    companion object {
        fun newInstance() = StatisticsFragment()
    }
}