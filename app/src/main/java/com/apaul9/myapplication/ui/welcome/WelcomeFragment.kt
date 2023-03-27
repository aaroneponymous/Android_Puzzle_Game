package com.apaul9.myapplication.ui.welcome

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.findNavController
import com.apaul9.myapplication.R
import com.apaul9.myapplication.ui.model.Jigsaw
import com.apaul9.myapplication.ui.model.PiecesData



class WelcomeFragment : Fragment() {

    // Variables for UI Elements
    private lateinit var statsButton: ImageButton
    private lateinit var settings: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var logoImage: ImageView

    // Variables for Jigsaw Puzzle Animation
    private lateinit var welcomeAnimImage: ImageView
    private lateinit var bitJigsawMap: Map<Int, PiecesData>
    private lateinit var jigsawAnimModel: Jigsaw


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Initialize the UI Elements
        statsButton = view.findViewById(R.id.statisticsBtn_imageButton)
        settings = view.findViewById(R.id.settingsBtn_imageButton)
        playButton = view.findViewById(R.id.playBtn_imageButton)
        logoImage = view.findViewById(R.id.logo_imageView)

        // Initialize the Welcome Animation Elements
        welcomeAnimImage = view.findViewById(R.id.welcome_imageView)
        jigsawAnimModel = Jigsaw(welcomeAnimImage, "None")

        // Calculate Dimension after the View is created
        puzzlePiecesMaker()
        puzzleAnimDisappear()
        // Make the other UI Elements Appear
        uiAppear()


        // Set the OnClickListeners for the UI Elements
        statsButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_statisticsFragment2)
        }

        settings.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_settingsFragment)
        }

        playButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_configFragment)
        }

        return view
    }


    private fun animatePuzzlePieces(jigsawPieces: List<Bitmap>, bitJigsawMap: HashMap<Int, PiecesData>, container: ViewGroup) {
        for (piece in jigsawPieces) {
            val originalPosition = bitJigsawMap[piece.hashCode()]
            if (originalPosition != null) {
                val puzzleView = ImageView(requireContext())
                puzzleView.setImageBitmap(piece)
                container.addView(puzzleView)
                val x = originalPosition.xCoord.toFloat()
                val y = originalPosition.yCoord.toFloat()
                puzzleView.x = x
                puzzleView.y = y
                val animatorX = ObjectAnimator.ofFloat(puzzleView, "x", x)
                val animatorY = ObjectAnimator.ofFloat(puzzleView, "y", y)
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animatorX, animatorY)
                animatorSet.duration = 500
                animatorSet.startDelay = jigsawPieces.indexOf(piece) * 50L
                animatorSet.start()
            }
        }
    }

    private fun puzzlePiecesMaker () {
        welcomeAnimImage.post {
            val modelSplitPair = jigsawAnimModel.splitImage()
            val jigsawPieces = modelSplitPair.first
            bitJigsawMap = modelSplitPair.second
            // Set visibility of the actual image to GONE
            welcomeAnimImage.visibility = View.GONE
            val viewPuzzles = view as ViewGroup
            animatePuzzlePieces(jigsawPieces, bitJigsawMap as HashMap<Int, PiecesData>, viewPuzzles)

        }
    }

    private fun puzzleAnimDisappear() {
        // Make the Welcome Animation Pieces Disappear
        Handler().postDelayed({
            val viewPuzzles = view as ViewGroup
            for (i in 0 until viewPuzzles.childCount) {
                val child = viewPuzzles.getChildAt(i)
                if (child is ImageView) {
                    child.animate().alpha(0f).setDuration(2000).start()
                }
            }
        }, 500)
    }

    private fun uiAppear() {
        Handler().postDelayed({
            statsButton.visibility = View.VISIBLE
            statsButton.animate().alpha(1f).setDuration(1000).start()
            settings.visibility = View.VISIBLE
            settings.animate().alpha(1f).setDuration(1000).start()
            playButton.visibility = View.VISIBLE
            playButton.animate().alpha(1f).setDuration(1000).start()
            logoImage.visibility = View.VISIBLE
            logoImage.animate().alpha(1f).setDuration(1000).start()
        }, 800)
    }

    companion object {
    }
}