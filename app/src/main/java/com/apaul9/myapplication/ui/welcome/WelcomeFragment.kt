package com.apaul9.myapplication.ui.welcome

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apaul9.myapplication.R


class WelcomeFragment : Fragment() {

    private lateinit var jigsawPieces: ArrayList<Bitmap>
    private lateinit var prefs: SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    companion object {
    }
}