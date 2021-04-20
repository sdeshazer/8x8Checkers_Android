package com.example.boardgame
import android.util.Log
import kotlin.math.abs

object CheckersPlay {
    // red player starts:
    var redTurn = true
    private var redCount = 0
    private var blackCount = 0
    private var totalPieces = mutableSetOf<CheckerPiece>()

    init{
        reset()
    }


    fun checkForWinner(): Int{
            redCount =0
            blackCount = 0
        for (piece in totalPieces){
            if(piece.player == Player.RED){
                redCount++
            }
            if(piece.player == Player.BLACK){
                blackCount++
            }
        }
        if(blackCount < 1){
            // red has won
            Log.d(TAG,"RED HAS WON" )
            return 1
        }
        if(redCount < 1){
            //black has won
            Log.d(TAG,"BLACK HAS WON" )
            return -1
        }
        return 0
    }


    //CheckerPiece(var col: Int, var row: Int, val player: Player, val ID: Int)
    fun reset() {
       totalPieces.clear()
        redTurn = true
        // where red is the player and black is the ai
        var red = R.drawable.red_piece

        var black = R.drawable.black_piece

        for(row in 0..2) {
            for (col in 0..7) { // fill bottom (player) rows pieces info:
                if (col % 2 == 0 && ((7-row) == 7 ||(7-row) == 5) ) {
                    totalPieces.add((CheckerPiece(col+1, 7-row, Player.BLACK, black)))

                } else if(col % 2 != 0 && (7-row) == 6) {
                    totalPieces.add(CheckerPiece(col-1, 7-row, Player.BLACK, black))
                }else if(col % 2 == 0){
                    totalPieces.add(CheckerPiece(col+1, row, Player.RED, red))
                }else{ // fill top row pieces info:
                    totalPieces.add(CheckerPiece(col-1, row, Player.RED, red))
                }
            }
        }
    }


    fun checkToKing() {
        for (piece in totalPieces) {
            if (piece.imageId == R.drawable.black_piece || piece.imageId == R.drawable.red_piece) {
                if (piece.player == Player.BLACK && piece.row == 0) {
                    var tempPiece = piece
                    totalPieces.remove(piece)
                    tempPiece.imageId = R.drawable.black_piece_king
                    totalPieces.add(tempPiece)

                }
                if (piece.player == Player.RED && piece.row == 7) {
                    var tempPiece = piece
                    totalPieces.remove(piece)
                    tempPiece.imageId = R.drawable.red_piece_king
                    totalPieces.add(tempPiece)
                }

            }
        }
    }


    fun pieceAt(square: Square): CheckerPiece?{
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col:Int, row: Int): CheckerPiece? {
        for (piece in totalPieces){

            if(col == piece.col && row == piece.row){
                return piece
            }
        }
        return null
    }

    fun movePiece(from: Square, to: Square){

        // check that the origin is not the same as the destination:
        if(canMove(from, to)){

            //check to see if he destination square is not occupied by another piece:
            if(squareIsClear(to.col, to.row)) {
                Log.d(TAG, "movePiece: moving from $from to $to")
                movePiece(from.col, from.row, to.col, to.row)
            }
        }
    }

    private fun movePiece(fromCol: Int, fromRow : Int, toCol: Int, toRow : Int){

        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            totalPieces.remove(it)
        }
        if(movingPiece.player == Player.BLACK && !redTurn){
            Log.d(TAG,"redTurn set to true")
            redTurn = true
        }
        if(movingPiece.player == Player.RED && redTurn){
            Log.d(TAG,"redTurn set to false")
            redTurn = false
        }
        totalPieces.remove(movingPiece)
        totalPieces.add(movingPiece.copy(col = toCol, row = toRow))

    }


    /*
     * check that the square we want to  move to is not occupied:
     */
    fun squareIsClear(toCol: Int, toRow: Int): Boolean{
        for (piece in totalPieces){
            if(piece.col == toCol && piece.row == toRow){
                return false
            }
        }

        return true
    }
    fun isMovingDown(from:Square, to:Square): Boolean{
        if(to.row < from.row){
            return true
        }
        return false
    }

    fun isMovingLeft(from:Square, to:Square): Boolean{
        if(to.col < from.col){
            return true
        }
        return false
    }

    /*
    * check that we can only move when all conditions are met based on either player:
    */
    fun canMove(from: Square, to: Square): Boolean {
        var centerRow = -1
        var movePiece: CheckerPiece? = pieceAt(from) ?: return false

        if(from.row == to.row){
           return false
        }

        // check if he destination is the same as the origin:
        if(from.col == to.col && from.row == to.row){
           return false
        }
        if(movePiece!!.player == Player.BLACK && redTurn){
            Log.d(TAG,"It is red's turn.")
            return false
        }
        if(movePiece!!.player == Player.RED && !redTurn){
            Log.d(TAG,"It is black's turn.")
            return false
        }
        if (movePiece != null) {

            // make sure we can only move a max of two squares at a time:
            if (abs(from.col - to.col) > 2 || abs(from.row - to.row) > 2) {
                return false
            }
            if (squareIsClear(to.col, to.row)) {
                // ensure that red can only up down a row, never backwards, unless king-ed.
                if (movePiece.player == Player.RED && movePiece.imageId != R.drawable.red_piece_king && from.row > to.row) {
                    return false
                }

                // ensure that black can only move down a row, never backwards, unless king-ed.
                if (movePiece.player == Player.BLACK && movePiece.imageId != R.drawable.black_piece_king && from.row < to.row) {
                    return false
                }

                if (abs(from.col - to.col) > 1) {
                    Log.d(TAG, "detected that a player skipped a column")

                    if (movePiece!!.player == Player.BLACK) {
                        // if the piece is black, is a king and is moving up:
                        if(movePiece.imageId == R.drawable.black_piece_king && !(isMovingDown(from,to))){
                            Log.d(TAG, "player is black, king-ed and moving up")
                            centerRow = from.row+1
                        }else {
                            // we know the piece is black but not king-ed, so its moving down:
                            centerRow = from.row - 1
                        }
                    //but if its red and king-ed and its moving up, we cet it to the previous row:
                    } else if(movePiece.imageId == R.drawable.red_piece_king && isMovingDown(from,to)) {
                        Log.d(TAG, "player is red, king-ed and moving up")
                        centerRow = from.row-1

                    }else{ // ensured that the piece is red but not king-ed:
                        centerRow = from.row+1
                    }
                    Log.d(TAG, "from $from to $to, centerRow $centerRow")
                    if (isMovingLeft(from, to)) {
                        var centerPieceMovingLeft = pieceAt(Square(from.col - 1, centerRow))
                        if (centerPieceMovingLeft != null&& centerPieceMovingLeft.player != movePiece.player) {
                            Log.d(TAG, "removed middle piece as a player moved right removed: $centerPieceMovingLeft")
                            totalPieces.remove(centerPieceMovingLeft)

                            return true
                        }
                    }
                    //ensured it's not moving to the left, we know its moving right:
                    var centerPieceMovingRight = pieceAt(Square(from.col + 1, centerRow))
                    if (centerPieceMovingRight != null && centerPieceMovingRight.player != movePiece.player) {
                        Log.d(TAG, "removed middle piece as a player moved right removed: $centerPieceMovingRight")
                        totalPieces.remove(centerPieceMovingRight)
                        return true
                    }else{
                        // if both the centerPieces on the left and right are null, we cannot do a double move:

                        return false
                    }
                }
            }
        }
        // this is to insure that the player cannot jump non-diagonally
        if(abs(to.row - from.row) >1 ){
            return false
        }
        // we simply move one square:
        return true
    }
}