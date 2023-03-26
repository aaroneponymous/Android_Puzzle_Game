package com.apaul9.myapplication.ui.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigViewModel : ViewModel() {


    private val _imageSelection = MutableLiveData<Int>()
    val imageSelection: LiveData<Int> = _imageSelection

    private val _modeSelection = MutableLiveData<String>()
    val modeSelection: LiveData<String> = _modeSelection

    // Functions to add to the view model
    fun setImageSelection(image: Int) {
        _imageSelection.value = image
    }

    fun setModeSelection(mode: String) {
        _modeSelection.value = mode
    }

}