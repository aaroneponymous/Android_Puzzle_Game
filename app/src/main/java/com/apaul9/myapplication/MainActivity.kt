package com.apaul9.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

/*
data class GridPosition(val row: Int, val col: Int)

private lateinit var pieceGridPositions: Map<Bitmap, GridPosition>

// In splitImage():
pieceGridPositions = HashMap()
var yCoord = 0
for (i in 0 until rows) {
    var xCoord = 0
    for (j in 0 until cols) {
        val piece = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight)
        pieces.add(piece)
        pieceGridPositions[piece] = GridPosition(i, j)
        xCoord += pieceWidth
    }
    yCoord += pieceHeight
}


val snapDistance = 100 // adjust this as needed
var originalX = 0f
var originalY = 0f
var inOriginalPosition = true

puzzleView.setOnTouchListener { v, event ->
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            originalX = v.x
            originalY = v.y
            inOriginalPosition = true
        }
        MotionEvent.ACTION_MOVE -> {
            val x = event.rawX - v.width / 2
            val y = event.rawY - v.height * 3 / 2
            v.x = x
            v.y = y
            if (inOriginalPosition && distance(originalX, originalY, x, y) > snapDistance) {
                inOriginalPosition = false
            }
        }
        MotionEvent.ACTION_UP -> {
            if (inOriginalPosition) {
                // piece is already in original position, do nothing
            } else {
                // snap piece back to original position
                val piece = (v as ImageView).drawable.toBitmap()
                val gridPosition = pieceGridPositions[piece]!!
                val (x, y) = calculatePieceLocation(gridPosition, v.width, v.height)
                v.animate().x(x).y(y).setDuration(200).start()
            }
        }
    }
    true
}

// Helper function to calculate the location of a piece in the puzzleFrameView based on its grid position
private fun calculatePieceLocation(gridPosition: GridPosition, pieceWidth: Int, pieceHeight: Int): Pair<Float, Float> {
    val frameDimensions = getBitPosInImageView(puzzleFrameView)!!
    val frameLeft = frameDimensions[0]
    val frameTop = frameDimensions[1]
    val frameWidth = frameDimensions[2]
    val frameHeight = frameDimensions[3]
    val x = frameLeft + gridPosition.col * (frameWidth / modelPuzzle.cols) + (frameWidth / modelPuzzle.cols - pieceWidth) / 2
    val y = frameTop + gridPosition.row * (frameHeight / modelPuzzle.rows) + (frameHeight / modelPuzzle.rows - pieceHeight) / 2
    return Pair(x, y)
}
*/

// Helper function to calculate the distance between two
