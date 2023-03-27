package com.apaul9.myapplication.ui.playgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayGameViewModel : ViewModel() {

    // Have to track games played, games won, and games lost
    // Have to track time played for each game as a pair of win/loss and time played long


    private val _gamesPlayed = MutableLiveData<Int>()
    val gamesPlayed: LiveData<Int> = _gamesPlayed

    private val _gamesWon = MutableLiveData<Int>()
    val gamesWon: LiveData<Int> = _gamesWon

    private val _gamesLost = MutableLiveData<Int>()
    val gamesLost: LiveData<Int> = _gamesLost

    private val _quicktimePlayed = MutableLiveData<Pair<Long, Long>>()
    val quicktimePlayed: MutableLiveData<Pair<Long, Long>> = _quicktimePlayed

   // Functions

    fun setGamesPlayed() {
        _gamesPlayed.value = _gamesPlayed.value?.plus(1)
    }

    fun setGamesWon() {
        _gamesWon.value = _gamesWon.value?.plus(1)
    }

    fun setGamesLost() {
        _gamesLost.value = _gamesLost.value?.plus(1)
    }

    // Function that takes in a parameter of Pair<Long, Long> and adds it to the list of pairs
    fun setQuicktimePlayed(time: Pair<Long, Long>) {
        _quicktimePlayed.value = time
    }

    // Function that returns the fastest time played
    // Return the lowest pair value, which is the time played
    fun getFastestTime(): Long {
        return _quicktimePlayed.value?.first ?: 0
    }


}
