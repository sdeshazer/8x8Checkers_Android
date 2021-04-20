package com.example.boardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.PrintWriter
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), PlayPiece{
    private var printWriter: PrintWriter? = null
    private lateinit var boardView: BoardView
    private lateinit var resetButton: Button
    private lateinit var resetScoreButton: Button
    private lateinit var scoreRed: TextView
    private lateinit var scoreBlack: TextView
    private lateinit var turnText: TextView
    private val isEmulator = Build.FINGERPRINT.contains("generic")
    private var blackScoreCount = 0
    private var redScoreCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardView = findViewById<BoardView>(R.id.board_view)
        resetButton = findViewById<Button>(R.id.reset_button)
        resetScoreButton = findViewById<Button>(R.id.reset_score_button)
        scoreRed = findViewById<TextView>(R.id.score_text_red)
        scoreBlack = findViewById<TextView>(R.id.score_text_black)
        turnText = findViewById<TextView>(R.id.turn_text)

        boardView.playPiece = this

        resetButton.setOnClickListener {
            CheckersPlay.reset()
            turnText.setText("******It is RED's Turn******")
            boardView.invalidate()
        }
        resetScoreButton.setOnClickListener{
            redScoreCount = 0
            blackScoreCount = 0
            scoreRed.setText("Red : $redScoreCount")
            scoreBlack.setText("Black : $blackScoreCount")

        }


    }

    override fun pieceAt(square: Square): CheckerPiece? = CheckersPlay.pieceAt(square)

    override fun movePiece(from: Square, to: Square) {
        CheckersPlay.movePiece(from, to)

        printWriter?.let {
            val moveStr = "${from.col},${from.row},${to.col},${to.row}"
            Log.d(TAG,moveStr)

        }
        if(CheckersPlay.checkForWinner() == -1){
            Log.d(TAG, "Black has won a game")
            blackScoreCount++
            scoreBlack.setText("Black : $blackScoreCount")
        }
        if(CheckersPlay.checkForWinner() == 1){
            Log.d(TAG, "Red has won a game")
            redScoreCount++
            scoreRed.setText("Red : $redScoreCount")

        }
        if(CheckersPlay.redTurn){
            turnText.setText("******It is RED's Turn******")
        }else{
            turnText.setText("******It is BLACKS's Turn******")
        }
        boardView.invalidate()
    }
}


