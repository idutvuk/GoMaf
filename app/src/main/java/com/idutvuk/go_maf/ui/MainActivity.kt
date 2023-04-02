package com.idutvuk.go_maf.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kmafia.R
import com.idutvuk.go_maf.model.Game

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        Game.numPlayers = 10
        Game.startGame()
    }

    override fun onDestroy() {
        Game.endGame()
        super.onDestroy()
    }
}