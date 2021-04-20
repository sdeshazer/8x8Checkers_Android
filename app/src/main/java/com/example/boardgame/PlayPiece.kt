package com.example.boardgame

interface PlayPiece {
    fun pieceAt(square: Square): CheckerPiece? //null check
    fun movePiece(from: Square, to: Square)

}

