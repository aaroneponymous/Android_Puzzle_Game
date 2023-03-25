package com.apaul9.myapplication.ui.model

data class PiecesData (
    val xCoord : Int,
    val yCoord : Int,
    val pieceWidth : Int,
    val pieceHeight : Int,
    var canDrag : Boolean = true
)
