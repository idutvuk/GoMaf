package com.idutvuk.go_maf.legacy

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.legacy.Game

@Deprecated("Use compose")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        Game.numPlayers = 6
    }
}