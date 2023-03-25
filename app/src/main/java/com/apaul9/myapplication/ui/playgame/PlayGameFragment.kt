package com.apaul9.myapplication.ui.playgame


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.apaul9.myapplication.ui.model.PiecesData
import com.apaul9.myapplication.ui.model.Jigsaw
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


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
//        modelPuzzle = Jigsaw(puzzleFrameView, 12, 4, 3)
        // Calculate Dimension after the View is created
        puzzleFrameView.post {
//            jigsawPieces = modelPuzzle.splitImage()
            jigsawPieces = splitImage(puzzleFrameView,12, 4, 3)
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


    // TODO: Change this for a general purpose function
    // TODO: HARD MODE, MEDIUM MODE & EASY MODE

    // SplitImage takes an ImageView and returns an ArrayList of Bitmaps
    private fun splitImage(imageView: ImageView, piecesNumber: Int, rows: Int, cols: Int):
            ArrayList<Bitmap> {

        val pieces: ArrayList<Bitmap> = ArrayList(piecesNumber)

        // Get the scaled bitmap of the source image
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val dimension = getBitPosInImageView(imageView)
        val scaledBitmapLeft = dimension!![0]
        val scaledBitmapTop = dimension!![1]
        val scaledBitmapWidth = dimension!![2]
        val scaledBitmapHeight = dimension!![3]

        val croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft)
        val croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop)

        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth,
            scaledBitmapHeight, true)
        val croppedBitmap: Bitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft),
            abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight)


        // Calculate the width and height of the pieces
        val pieceWidth = croppedImageWidth / cols
        val pieceHeight = croppedImageHeight/ rows
        // create a Map of the Bitmaps and their Positions
        val bitImageMap = mutableMapOf<Int, PiecesData>()

        // Create each bitmap piece and add it to the resulting array
        var yCoord = 0
        for (i in 0 until rows) {
            var xCoord = 0
            for (j in 0 until cols) {
                val jigsawPiece = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight)
                // Adjust the pieces position values to the bounds of the ImageView
                val xBoundedCoord = xCoord + imageView.left
                val yBoundedCoord = yCoord + imageView.top
                // Create a PiecesData Object to store the position and size of the pieces
                val piecesData = PiecesData(xBoundedCoord, yBoundedCoord, pieceWidth, pieceHeight)
                bitImageMap[jigsawPiece.hashCode()] = piecesData
                pieces.add(jigsawPiece)
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }
        bitJigsawMap = bitImageMap
        return pieces
    }

    // Calculating the scaled Dimensions and Positions of the Pieces
    // Setting them into the ImageView
    private fun getBitPosInImageView(imageView: ImageView?): IntArray? {
        val ret = IntArray(4)
        if (imageView == null || imageView.drawable == null) return ret

        // Get image dimensions
        // Get image matrix values and place them in an array
        val f = FloatArray(9)
        imageView.imageMatrix.getValues(f)

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        val d = imageView.drawable
        val origW = d.intrinsicWidth
        val origH = d.intrinsicHeight

        // Calculate the actual dimensions
        val actW = (origW * scaleX).roundToInt()
        val actH = (origH * scaleY).roundToInt()
        ret[2] = actW
        ret[3] = actH

        // Get image position
        // We assume that the image is centered into ImageView
        val imgViewW = imageView.width
        val imgViewH = imageView.height
        val top = (imgViewH - actH) / 2
        val left = (imgViewW - actW) / 2
        ret[0] = left
        ret[1] = top
        return ret
    }

    companion object {
    }
}