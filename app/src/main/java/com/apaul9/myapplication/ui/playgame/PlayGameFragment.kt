package com.apaul9.myapplication.ui.playgame


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.apaul9.myapplication.R
import com.apaul9.myapplication.ui.model.Jigsaw
import kotlin.math.abs
import kotlin.math.roundToInt


class PlayGameFragment : Fragment() {


    private lateinit var jigsawPieces: ArrayList<Bitmap>
    private lateinit var prefs: SharedPreferences
    private lateinit var puzzleFrameView: ImageView
    private lateinit var modelPuzzle: Jigsaw



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_play_game, container, false)
        prefs = PreferenceManager.getDefaultSharedPreferences(view.context)
        puzzleFrameView = view.findViewById(R.id.puzzle_imageView)
        modelPuzzle = Jigsaw(puzzleFrameView, 30, 10, 3)
        // Calculate Dimension after the View is created
        puzzleFrameView.post {
            jigsawPieces = modelPuzzle.splitImage()
            for (piece in jigsawPieces) {
                val puzzleView = ImageView(requireContext())
                puzzleView.setImageBitmap(piece)
                puzzleView.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_MOVE -> {
                            // TODO: Implement onTouchMove logic
                            val x = event.rawX - v.width / 2
                            val y = event.rawY - v.height * 3 / 2
                            v.x = x
                            v.y = y
                        }
                    }
                    true
                }
                (view as ViewGroup).addView(puzzleView)
            }
        }
        return view
    }

    companion object {
    }
}