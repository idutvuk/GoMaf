package com.idutvuk.go_maf.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.gameactions.gamestate.GameEndAction
import com.idutvuk.go_maf.model.gameactions.gamestate.GameStartAction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        Game.numPlayers = 10
        CmdManager.commit(GameStartAction())
    }

    override fun onDestroy() {
        //TODO: Remove logic from the activity (game restarts when I rotate the phone)
        CmdManager.commit(GameEndAction())
        super.onDestroy()
    }
}