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
    private val numPlayers = 10
//    private var curBtn = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentGameBinding.inflate(layoutInflater)
        Log.i("GameLog","view created")
        b.btnDState.setOnClickListener { Game.getState() }


        Game.playerNumber = 10
        Game.startGame()
        val points = generatePivotPoints(numPlayers)

        val buttons = listOf(
            b.btn1, b.btn2, b.btn3,
            b.btn4, b.btn5, b.btn6,
            b.btn7, b.btn8, b.btn9,
            b.btn10, b.btn11,b.btn12
        )

        for(index in 0 until numPlayers) {
            buttons[index].x += points[index][0].toFloat()
            buttons[index].y += points[index][1].toFloat()
        }

        for (i in numPlayers until 12) {
            buttons[i].visibility = View.GONE
            Log.d("GraphLog", "Button $i deleted.")
        }


        Log.d("GraphLog","width ${b.root.width}")
        return b.root
    }


    override fun onDestroyView() {
        Game.endGame()
        super.onDestroyView()
    }
    private fun generatePivotPoints(
        numPlayers: Int,
        radius: Int = 340
    ): Array<IntArray> {
        val pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }

        val angleOffset = Math.toRadians(60.0)
        for (i in 0 until numPlayers) {
            val angle =
                ((2 * Math.PI - angleOffset) / (numPlayers - 1) * i + angleOffset / 2).toFloat()

            val x = -(radius * sin(angle.toDouble())).toInt()
            val y = (radius * cos(angle.toDouble())).toInt()
            pivotPoints[i] = intArrayOf(x, y)
            Log.d("GraphLog", "index: $i x: $x y: $y")
        }
        return pivotPoints
    }



}