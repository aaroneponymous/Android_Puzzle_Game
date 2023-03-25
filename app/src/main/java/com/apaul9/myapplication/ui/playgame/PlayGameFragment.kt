package com.apaul9.myapplication.ui.playgame


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.*
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
import com.apaul9.myapplication.ui.model.PiecesData
import kotlin.math.*


class PlayGameFragment : Fragment() {


    private lateinit var jigsawPieces: ArrayList<Bitmap>
    private lateinit var prefs: SharedPreferences
    private lateinit var puzzleFrameView: ImageView
    private lateinit var modelPuzzle: Jigsaw
    private lateinit var bitJigsawMap: Map<Int,PiecesData>




    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_play_game, container, false)
        prefs = PreferenceManager.getDefaultSharedPreferences(view.context)
        puzzleFrameView = view.findViewById(R.id.puzzle_imageView)
        modelPuzzle = Jigsaw(puzzleFrameView, 12, 4, 3)
        // Calculate Dimension after the View is created
        puzzleFrameView.post {
            val modelSplitPair = modelPuzzle.splitImage(puzzleFrameView, 12, 4, 3)
            jigsawPieces = modelSplitPair.first
            bitJigsawMap = modelSplitPair.second
            // Set visibility of the actual image to GONE
            puzzleFrameView.visibility = View.GONE
            for (piece in jigsawPieces) {
                val puzzleView = ImageView(requireContext())
                puzzleView.setImageBitmap(piece)
                puzzleView.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_MOVE -> {
                            // Check if the piece can be dragged
                            val originalPosition = bitJigsawMap[piece.hashCode()]
                            if (originalPosition != null) {
                                if (originalPosition.canDrag) {
                                    val x = event.rawX - v.width / 2
                                    val y = event.rawY - v.height * 3 / 2
                                    v.x = x
                                    v.y = y
                                }
                            }

                        }
                        MotionEvent.ACTION_UP -> {
                            // Look up the ImageBitmap in the Map
                            // Compare the Positions through the Map Value
                            // which is a PiecesData Object
                            val originalPosition = bitJigsawMap[piece.hashCode()]
                            if (originalPosition != null) {
                                val distance = sqrt(
                                    (v.x - originalPosition.xCoord).pow(2) +
                                            (v.y - originalPosition.yCoord).pow(2)
                                )
                                val threshold = v.width / 2
                                if (distance < threshold) {
                                    // First: Change the boolean value of the piece
                                    // canDrag to false and modify the Map
                                    originalPosition.canDrag = false
                                    // Second: Snap the jigsaw piece back to its original position
                                    v.animate().x(originalPosition.xCoord.toFloat()).y(originalPosition.yCoord.toFloat())
                                        .setDuration(100).start()
                                }
                            }

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