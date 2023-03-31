package com.idutvuk.go_maf

import CmdManager
import FoulAction
import KillAction
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kmafia.databinding.FragmentGameBinding
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {
    private val numPlayers = 10
    private var logMsg = ""
    private var curBtn = "none"
    lateinit var b:FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentGameBinding.inflate(layoutInflater)
        Log.i("GameLog","view created")
        CmdManager.gameFragment = this
        Game.buttons = listOf(
            b.btn1, b.btn2, b.btn3,b.btn4, b.btn5, b.btn6,
            b.btn7, b.btn8, b.btn9,b.btn10, b.btn11,b.btn12
        )

        SmartTV.tvPrimary = b.tvBig
        SmartTV.tvSecondary = b.tvUpper


        val points = generatePivotPoints(numPlayers)
        for (i in 12-1 downTo numPlayers) {
            Game.buttons[i].visibility = View.GONE
            logMsg += "Btn '${i + 1}' hidden\n"
        }
        for(i in 0 until numPlayers) {
            val x = points[i][0].toFloat()
            val y = points[i][1].toFloat()
            Game.buttons[i].x += x
            Game.buttons[i].y += y
            logMsg+="Btn '${i+1}' added with relative x: $x, y:$y\n"
            Game.buttons[i].setOnClickListener{v ->
                when(curBtn) {
                    "none"-> {}
                    "kill"-> {
                        CmdManager.commit(KillAction(i))
                           v.isEnabled = false
                    }
                    "vote"-> {
                        TODO("Not yet implemented")
                    }
                    "foul"-> {
                        CmdManager.commit(FoulAction(i))
                    }
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
                // Change the text of all Game.buttons when the FAB is pressed
                for (i in 0 until numPlayers)
                    Game.buttons[i].text = Game.players[i].emoji
                view.performClick()
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                // Change the text of all Game.buttons back to their old style when the FAB is released
                for (i in 0 until numPlayers)
                    Game.buttons[i].text = (i+1).toString()
                view.performClick()
            }
            false
        }

        b.btnUndo.setOnClickListener{
            CmdManager.undo()
        }
        b.btnRedo.setOnClickListener{
            CmdManager.redo()
        }

        b.btnDState.setOnClickListener {
            Game.getState()
        }

        b.btnDUniversal.setOnClickListener{
           SmartTV.blink(2000)
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

    fun undoButtonState(state: Boolean) {
        b.btnUndo.isEnabled = state
    }
    fun redoButtonState(state: Boolean) {
        b.btnRedo.isEnabled = state
    }
}