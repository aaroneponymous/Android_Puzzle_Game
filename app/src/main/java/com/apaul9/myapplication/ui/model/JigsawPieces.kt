package com.apaul9.myapplication.ui.model
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView

class JigsawPieces(context: Context) : AppCompatImageView(context) {
    var xCoord: Int = 0
    var yCoord: Int = 0
    var pieceWidth: Int = 0
    var pieceHeight: Int = 0
    var canMove: Boolean = true
}