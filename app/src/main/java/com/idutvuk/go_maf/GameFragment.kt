package com.idutvuk.go_maf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kmafia.databinding.FragmentGameBinding
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {


    private val selectionTimeMillis = 3000

    // Define the number of player circles and the radius of the table
    private val numPlayers = 10
//    private val underTableRadiusDp = dp2px(180)
//    private val tableRadiusPx = dp2px(130)

    //declared here bcs I have to use them outside the OnCreate method.
    private var pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }
    private var players: Array<L_Player> = Array(numPlayers) { L_Player() }
    private var playerButtons: ArrayList<View> = ArrayList() //TODO: replace
    private var curBtn = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentGameBinding.inflate(layoutInflater)
        Log.i("GameLog","view created")
        b.btnDState.setOnClickListener { Game.getState() }


        Game.playerNumber = 10
        Game.startGame()
        //test moving
        b.btn1.x = 100F
        b.btn1.y = 50F

        return b.root
    }

    override fun onDestroyView() {
        Game.endGame()
        super.onDestroyView()
    }

}