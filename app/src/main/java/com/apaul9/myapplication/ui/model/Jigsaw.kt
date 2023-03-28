package com.apaul9.myapplication.ui.model

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class Jigsaw (imageView: ImageView, gameMode: String) {


    private var imageChosen: ImageView = imageView
    private var gameMode: String = gameMode

    // Implement a function that takes in game difficulty and returns the number of pieces
    // and the number of rows and columns as a Triple
    private fun getDifficulty(difficulty: String): Triple<Int, Int, Int> {
        return when (difficulty) {
            "easy" -> Triple(20, 5, 4)
            "medium" -> Triple(30, 6, 5)
            else -> Triple(42, 7, 6)
        }
    }

    // Implement a function that returns time in milliseconds for the game
    fun getTime(difficulty: String): Long {
        return when (difficulty) {
            "easy" -> 3 * 60 * 1000L // 4 minutes
            "medium" -> 1 * 1 * 1000L // 3 minutes
            else -> 1 * 60 * 1000L // 2 minutes
        }
    }

    // SplitImage takes an ImageView and returns an ArrayList of Bitmaps
    fun splitImage(): Pair<ArrayList<Bitmap>, MutableMap<Int, PiecesData>> {
        // Create a Triple int of the number of pieces, rows, and columns
        val difficulty = getDifficulty(gameMode)
        val piecesNumber = difficulty.first
        val rows = difficulty.second
        val columns = difficulty.third


        val pieces: ArrayList<Bitmap> = ArrayList(piecesNumber)


        // Get the scaled bitmap of the source image
        val drawable = imageChosen.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val dimension = getBitPosInImageView(imageChosen)
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
        val pieceWidth = croppedImageWidth / columns
        val pieceHeight = croppedImageHeight/ rows
        // create a Map of the Bitmaps and their Positions
        val bitImageMap = mutableMapOf<Int, PiecesData>()
        val bitImageMapSimple = mutableMapOf<Int, PiecesData>()

        // Create each bitmap piece and add it to the resulting array
        var yCoord = 0
        for (r in 0 until rows) {
            var xCoord = 0
            for (c in 0 until columns) {
                // calculate offset for each piece
                // calculate offset for each piece
                var offsetX = 0
                var offsetY = 0
                if (c > 0) {
                    offsetX = pieceWidth / 3
                }
                if (r > 0) {
                    offsetY = pieceHeight / 3
                }
                val jigsawPiece = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX,
                    yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY)

                // Adjust the pieces position values to the bounds of the ImageView
                val xBoundedCoord = xCoord - offsetX + imageChosen.left
                val yBoundedCoord = yCoord - offsetY + imageChosen.top
                val pieceWidthOffSet = pieceWidth + offsetX
                val pieceHeightOffSet = pieceHeight + offsetY

                val jigsawPieceMaker = Bitmap.createBitmap(pieceWidth + offsetX,
                    pieceHeight + offsetY, Bitmap.Config.ARGB_8888)

                // draw path
                var bumpSize = pieceHeight / 4;
                val canvas = Canvas(jigsawPieceMaker)
                val path = Path()
                path.moveTo(offsetX.toFloat(), offsetY.toFloat())

                if (r == 0) {
                    // top side piece
                    path.lineTo(jigsawPiece.width.toFloat(), offsetY.toFloat());
                } else {
                    // top bump
                    path.lineTo(offsetX + (jigsawPiece.width.toFloat() - offsetX) / 3,
                        offsetY.toFloat());
                    path.cubicTo(offsetX + (jigsawPiece.width.toFloat() - offsetX) / 6,
                        offsetY.toFloat() - bumpSize, offsetX + (jigsawPiece.width.toFloat() - offsetX) / 6 * 5,
                        offsetY.toFloat() - bumpSize, offsetX + (jigsawPiece.width.toFloat() - offsetX) / 3 * 2, offsetY.toFloat());
                    path.lineTo(jigsawPiece.width.toFloat(), offsetY.toFloat())
                }

                if (c == columns - 1) {
                    // right side piece
                    path.lineTo(jigsawPiece.width.toFloat(), jigsawPiece.height.toFloat());
                } else {
                    // right bump
                    path.lineTo(jigsawPiece.width.toFloat(), offsetY + (jigsawPiece.height.toFloat() - offsetY) / 3);
                    path.cubicTo(jigsawPiece.width.toFloat() - bumpSize,offsetY + (jigsawPiece.height.toFloat() - offsetY) / 6,
                        jigsawPiece.width.toFloat() - bumpSize, offsetY + (jigsawPiece.height.toFloat() - offsetY) / 6 * 5, jigsawPiece.width.toFloat(),
                        offsetY + (jigsawPiece.height.toFloat() - offsetY) / 3 * 2);
                    path.lineTo(jigsawPiece.width.toFloat(), jigsawPiece.height.toFloat());
                }

                if (r == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX.toFloat(), jigsawPiece.height.toFloat());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (jigsawPiece.width.toFloat() - offsetX) / 3 * 2, jigsawPiece.height.toFloat());
                    path.cubicTo(offsetX + (jigsawPiece.width.toFloat() - offsetX) / 6 * 5,jigsawPiece.height.toFloat() - bumpSize,
                        offsetX + (jigsawPiece.width.toFloat() - offsetX) / 6, jigsawPiece.height.toFloat() - bumpSize,
                        offsetX + (jigsawPiece.width.toFloat() - offsetX) / 3, jigsawPiece.height.toFloat());
                    path.lineTo(offsetX.toFloat(), jigsawPiece.height.toFloat());
                }

                if (c == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX.toFloat(), offsetY + (jigsawPiece.height.toFloat() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX.toFloat() - bumpSize, offsetY + (jigsawPiece.height.toFloat() - offsetY) / 6 * 5,
                        offsetX.toFloat() - bumpSize, offsetY + (jigsawPiece.height.toFloat() - offsetY) / 6, offsetX.toFloat(),
                        offsetY + (jigsawPiece.height.toFloat() - offsetY) / 3);
                    path.close();
                }

                // mask the piece
                val gradient = LinearGradient(0f, 0f, 0f, jigsawPiece.height.toFloat(),
                    intArrayOf(-0x1, -0x1000000), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
                val paint = Paint()
                paint.color = -0x1000000
                paint.shader = gradient

                canvas.drawPath(path, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                canvas.drawBitmap(jigsawPiece, (0).toFloat(), (0).toFloat(), paint)

                // draw highlights
                val lightAngle = 45 // angle of light source in degrees
                val lightDir = Point(
                    cos(Math.toRadians(lightAngle.toDouble())).toFloat().toInt(),
                    sin(Math.toRadians(lightAngle.toDouble())).toFloat().toInt()
                ) // direction vector of light source
                val normal = Point(
                    (-(offsetY + jigsawPiece.height / 2 - imageChosen.height / 2).toFloat()).toInt(),
                    (offsetX + jigsawPiece.width / 2 - imageChosen.width / 2).toFloat().toInt()
                ) // normal vector of piece
                val dotProduct = lightDir.x * normal.x + lightDir.y * normal.y // dot product of direction and normal
                if (dotProduct > 0) {
                    // draw a lighter color on the edges that face the light source
                    val highlight = Paint()
                    highlight.color = Color.WHITE
                    highlight.style = Paint.Style.STROKE
                    highlight.strokeWidth = 3.0f
                    canvas.drawPath(path, highlight)
                }

                // draw a white border
                var border = Paint()
                border.color = Color.LTGRAY
                border.style = Paint.Style.STROKE
                border.strokeWidth = 3.0f
                canvas.drawPath(path, border)


                // draw a black border
                border = Paint()
                border.color = Color.BLACK
                border.style = Paint.Style.STROKE
                border.strokeWidth = 1.0f
                canvas.drawPath(path, border)

                // draw a shadow
                val lightDir2 = Point(
                    cos(Math.toRadians(lightAngle.toDouble())).toFloat().toInt(),
                    sin(Math.toRadians(lightAngle.toDouble())).toFloat().toInt()
                ) // direction vector of light source
                val normal1 = Point(
                    (-(offsetY + jigsawPiece.height / 2 - imageChosen.height / 2).toFloat()).toInt(),
                    (offsetX + jigsawPiece.width / 2 - imageChosen.width / 2).toFloat().toInt()
                ) // normal vector of piece
                val dotProduct1 = lightDir.x * normal.x + lightDir.y * normal.y // dot product of direction and normal
                if (dotProduct < 0) {
                    // draw a lighter color on the edges that face the light source
                    val shadow = Paint()
                    shadow.color = Color.BLACK
                    shadow.style = Paint.Style.STROKE
                    shadow.strokeWidth = 3.0f
                    canvas.drawPath(path, shadow)
                }
                // Create a PiecesData Object to store the position and size of the pieces
                val piecesData = PiecesData(xBoundedCoord, yBoundedCoord, pieceWidthOffSet, pieceHeightOffSet)
                bitImageMap[jigsawPieceMaker.hashCode()] = piecesData

                pieces.add(jigsawPieceMaker)
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }

        // Return a pair of the pieces and the bitImageMap
        return Pair(pieces, bitImageMap)
    }

    // Calculate the scaled Dimensions and Positions of the Pieces
    private fun getBitPosInImageView(imageView: ImageView?): IntArray? {
        val rectangle = IntArray(4)
        if (imageView == null || imageView.drawable == null) return rectangle

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

        // Calculate the actualual dimensions
        val actualW = (origW * scaleX).roundToInt()
        val actualH = (origH * scaleY).roundToInt()
        rectangle[2] = actualW
        rectangle[3] = actualH

        // Get image position
        // We assume that the image is centered into ImageView
        val imgViewW = imageView.width
        val imgViewH = imageView.height
        val top = (imgViewH - actualH) / 2
        val left = (imgViewW - actualW) / 2
        rectangle[0] = left
        rectangle[1] = top
        return rectangle
    }
}