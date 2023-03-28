package com.apaul9.myapplication.ui.playgame


import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.apaul9.myapplication.R
import com.apaul9.myapplication.ui.configuration.ConfigViewModel
import com.apaul9.myapplication.ui.model.Jigsaw
import com.apaul9.myapplication.ui.model.PiecesData
import com.apaul9.myapplication.ui.model.Timer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*


class PlayGameFragment : Fragment() {


    private lateinit var jigsawPieces: ArrayList<Bitmap>
    private lateinit var prefs: SharedPreferences
    private lateinit var puzzleFrameView: ImageView
    private lateinit var modelPuzzle: Jigsaw
    private lateinit var piecesContainer: ImageView
    private lateinit var bitJigsawMap: Map<Int,PiecesData>
    private lateinit var timer: Timer
    private lateinit var timerTextView: TextView
    private lateinit var winLoss: ImageView
    private lateinit var newGameBtn: Button


    private val viewModelGameConfig: ConfigViewModel by navGraphViewModels(R.id.nav_graph)
    private val viewModelGameStats: PlayGameViewModel by navGraphViewModels(R.id.nav_graph)





    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_play_game, container, false)
        prefs = PreferenceManager.getDefaultSharedPreferences(view.context)
        puzzleFrameView = view.findViewById(R.id.puzzle_imageView)
        timerTextView = view.findViewById(R.id.timer_textView)
        piecesContainer = view.findViewById(R.id.piecesContainer_imageView)
        winLoss = view.findViewById(R.id.winLoss_imageView)
        newGameBtn = view.findViewById(R.id.newGame_button)

        // Set On Click Listeners for Buttons
        newGameBtn.setOnClickListener {
            // Navigate to the Configuration Fragment
            view.findNavController().navigate(R.id.action_playGameFragment_to_welcomeFragment)
        }


        val gameDifficulty: String = viewModelGameConfig.modeSelection.value.toString()

        puzzleFrameView.setImageResource(viewModelGameConfig.imageSelection.value!!)
        modelPuzzle = Jigsaw(puzzleFrameView, gameDifficulty)
        // Calculate Dimension after the View is created
        puzzleFrameView.post {
            val modelSplitPair = modelPuzzle.splitImage()
            jigsawPieces = modelSplitPair.first
            bitJigsawMap = modelSplitPair.second
            // Set visibility of the actual image to GONE
            puzzleFrameView.visibility = View.GONE
            jigsawPieces.shuffle()
            for (piece in jigsawPieces) {
                val puzzleView = ImageView(requireContext())
                // Randomize the position of the jigsaw pieces
                // Within the constraints of the piecesContainer
                val randomX = Random().nextInt(piecesContainer.width - piece.width) + piecesContainer.left
                val randomY = Random().nextInt(piecesContainer.height - piece.height) + piecesContainer.top
                puzzleView.x = randomX.toFloat()
                puzzleView.y = randomY.toFloat()

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
                                    // And send the piece to the back of the view hierarchy
                                    v.z = -1f
                                    // Third: Check if all pieces are in their original position
                                    // If so, stop the timer and animate the win/loss image
                                    if (checkIfSolved()) {
                                        viewModelGameStats.setQuicktimePlayed(modelPuzzle.getTime(gameDifficulty)-timer.getTimeLeft())

                                        if (viewModelGameStats.gamesWon.value == null) {
                                            viewModelGameStats.setGamesWon(1)
                                        } else {
                                            val gamesWon = viewModelGameStats.gamesWon.value
                                            viewModelGameStats.setGamesWon(gamesWon?.plus(1) ?: 0)

                                        }

                                        if (viewModelGameStats.gamesPlayed.value == null) {
                                            viewModelGameStats.setGamesPlayed(1)
                                        } else {
                                            val gamesPlayed = viewModelGameStats.gamesPlayed.value
                                            viewModelGameStats.setGamesPlayed(gamesPlayed?.plus(1) ?: 0)
                                        }
                                        timer.stop()
                                        winLoss.setImageResource(R.drawable.winpuzzle)
                                        animateWinLossImage(winLoss)
                                        newGameBtn.visibility = View.VISIBLE
                                    }
                                } // and if not in the threshold place piece animates back to the position it was dragged from
                                else {
                                    v.animate().x(randomX.toFloat()).y(randomY.toFloat())
                                        .setDuration(100).start()
                                }
                            }

                        }
                    }
                    true
                }
                (view as ViewGroup).addView(puzzleView)

                // Create the timer with the total time and callbacks
                timer = Timer(modelPuzzle.getTime(gameDifficulty), { _ ->
                        val (minutes, seconds) = timer.getTimeRemaining()
                        timerTextView.text = "%02d:%02d".format(minutes, seconds) },
                    { // Timer finished, disable piece dragging here
                        for (piece in jigsawPieces) {
                            val originalPosition = bitJigsawMap[piece.hashCode()]
                            if (originalPosition != null) {
                                originalPosition.canDrag = false }
                        }

                        viewModelGameStats.setGamesPlayed(1)
                        viewModelGameStats.setGamesLost(1)
                        // Change the color of the timer to red
                    timerTextView.setTextColor(Color.RED)
                    // Animate the win/loss image
                    winLoss.setImageResource(R.drawable.ranout)
                    animateWinLossImage(winLoss)
                    newGameBtn.visibility = View.VISIBLE
                    })

                // Start the timer
                timer.start()

            }
        }
        return view
    }


    // Function to check if the puzzle is solved
    private fun checkIfSolved(): Boolean {
        for (piece in jigsawPieces) {
            val originalPosition = bitJigsawMap[piece.hashCode()]
            if (originalPosition != null) {
                if (originalPosition.canDrag) {
                    return false
                }
            }
        }
        return true
    }

    // Animate Win or Loss Image Using ObjectAnimator
    // Make the image invisible after the animation is finished
    private fun animateWinLossImage(image: ImageView) {
        // Bring the image to the front
        image.bringToFront()

        val animatorSet = AnimatorSet()
        val animatorZoomIn = ObjectAnimator.ofFloat(image, "scaleX", 0f, 1f)
        val animatorZoomOut = ObjectAnimator.ofFloat(image, "scaleX", 1f, 0f)
        val animatorShake = ObjectAnimator.ofFloat(image, "translationX", 0f, 50f, 0f, -50f, 0f)
        animatorSet.play(animatorZoomIn).with(animatorZoomOut).with(animatorShake)
        animatorSet.duration = 3000
        animatorSet.start()


        // Slowly fade out the image at the end of the animation
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animator) {
                image.animate().alpha(0f).duration = 3000
            }

            override fun onAnimationCancel(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator) {
                TODO("Not yet implemented")
            }
        })

    }

    companion object {
    }
}