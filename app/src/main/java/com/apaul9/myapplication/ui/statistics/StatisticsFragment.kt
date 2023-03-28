package com.apaul9.myapplication.ui.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.navGraphViewModels
import com.apaul9.myapplication.R
import com.apaul9.myapplication.ui.playgame.PlayGameViewModel


class StatisticsFragment : Fragment() {

    private val statsViewModel: PlayGameViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var totalGame: TextView
    private lateinit var winCount: TextView
    private lateinit var loseCount: TextView
    private lateinit var quickTime: TextView
    private lateinit var resetBtn: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Initialize the lateinit var here
        totalGame = view.findViewById(R.id.gamesPlayedCount_textView)
        winCount = view.findViewById(R.id.won_textView)
        loseCount = view.findViewById(R.id.lost_textView)
        quickTime = view.findViewById(R.id.fastest_time_textView)

        statsViewModel.gamesPlayed.observe(viewLifecycleOwner) { gamesPlayed ->
            totalGame.text = gamesPlayed.toString()
        }

        statsViewModel.gamesWon.observe(viewLifecycleOwner) { gamesWon ->
            winCount.text = gamesWon.toString()
        }
        statsViewModel.gamesLost.observe(viewLifecycleOwner) { gamesLost ->
            loseCount.text = gamesLost.toString()
        }

        statsViewModel.quicktimePlayed.observe(viewLifecycleOwner) { quicktimePlayed ->
            if (quicktimePlayed != Long.MAX_VALUE)
                quickTime.text = convertLongToTime(quicktimePlayed)
            else
                quickTime.text = "None"
        }

        resetBtn = view.findViewById(R.id.resetStats_button)
        resetBtn.setOnClickListener {
            resetStats()
        }


        return view

    }

    private fun resetStats() {
        statsViewModel.reset()
        // Set the textViews to None
        totalGame.text = "0"
        winCount.text = "0"
        loseCount.text = "0"
        quickTime.text = "None"
    }

    // Method to convert Long to "00:00" format
    private fun convertLongToTime(time: Long): String {
        val minutes = time / 1000 / 60
        val seconds = time / 1000 % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    companion object {
        fun newInstance() = StatisticsFragment()
    }
}