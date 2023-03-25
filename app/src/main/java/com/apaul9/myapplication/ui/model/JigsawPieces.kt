package com.apaul9.myapplication.ui.model

data class JigsawPieces (
    var xCoord: Int,
    var yCoord: Int,
    var pieceWidth: Int,
    var pieceHeight: Int,
    var canDrag: Boolean = true
)
