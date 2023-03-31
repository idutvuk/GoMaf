package com.idutvuk.go_maf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kmafia.databinding.FragmentGameBinding
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {
    private val numPlayers = 10
    private var logMsg = ""
    private var curBtn = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentGameBinding.inflate(layoutInflater)
        Log.i("GameLog","view created")
        b.btnDState.setOnClickListener { Game.getState() }

        val buttons = listOf(
            b.btn1, b.btn2, b.btn3,b.btn4, b.btn5, b.btn6,
            b.btn7, b.btn8, b.btn9,b.btn10, b.btn11,b.btn12
        )
        val points = generatePivotPoints(numPlayers)
        for (i in 12-1 downTo numPlayers) {
            buttons[i].visibility = View.GONE
            logMsg += "Btn '${i + 1}' hidden\n"
        }
        for(i in 0 until numPlayers) {
            val x = points[i][0].toFloat()
            val y = points[i][1].toFloat()
            buttons[i].x += x
            buttons[i].y += y
            logMsg+="Btn '${i+1}' added with relative x: $x, y:$y\n"
            buttons[i].setOnClickListener{v ->
                when(curBtn) {
                    "none"->""
                    "kill"-> {
                        if (Game.kill(i))
                           v.isEnabled = false
                    }
                    "vote"-> Log.i("GameLog","voted ${i+1}")
                    "foul"-> Game.players[i].foul()
                    else -> Log.e("GameLog", "Incorrect curBtn type")
                }
                curBtn="none"
            }
        }

        Log.d("GraphLog", logMsg); logMsg=""

        //TODO make auto mode droppers
        b.btnKill.setOnClickListener{
            curBtn="kill"
        }

        b.btnVote.setOnClickListener{
            curBtn="vote"
        }

        b.btnFoul.setOnClickListener{
            curBtn="foul"
        }
        b.btnPeep.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                // Change the text of all buttons when the FAB is pressed
                for (i in 0 until numPlayers) {
                    buttons[i].text = Game.players[i].emoji
                }
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                // Change the text of all buttons back to their old style when the FAB is released
                for (i in 0 until numPlayers) {
                    buttons[i].text = (i+1).toString()
                }
            }
            false
        }



        return b.root
    }


    override fun onDestroyView() {
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
        }
        return pivotPoints
    }
}