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

    private val _quicktimePlayed = MutableLiveData<Long>()
    val quicktimePlayed: LiveData<Long> = _quicktimePlayed

   // Functions

    fun setGamesPlayed(int : Int) {
        _gamesPlayed.value = int
    }

    fun setGamesWon(int: Int) {
        _gamesWon.value = int
    }

    fun setGamesLost(int: Int) {
        _gamesLost.value = int
    }

    fun setQuicktimePlayed(timePlayed: Long) {
        // Make sure if the value is null, set it to the time played
        if (_quicktimePlayed.value == null) {
            _quicktimePlayed.value = timePlayed
        } else if (timePlayed < _quicktimePlayed.value!!) {
            _quicktimePlayed.value = timePlayed
        }
    }



    // Function to reset all the values
    fun reset() {
        _gamesPlayed.value = 0
        _gamesWon.value = 0
        _gamesLost.value = 0
        // Set the quicktime to max long value
        _quicktimePlayed.value = Long.MAX_VALUE
    }


}
