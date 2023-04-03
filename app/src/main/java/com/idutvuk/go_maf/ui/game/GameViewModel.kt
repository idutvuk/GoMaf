package com.idutvuk.go_maf.ui.game


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.RecyclerViewLogAdapter
import com.idutvuk.go_maf.model.gameactions.AddToVoteAction
import com.idutvuk.go_maf.model.gameactions.FoulAction
import com.idutvuk.go_maf.model.gameactions.KillAction
import kotlin.math.cos
import kotlin.math.sin


class GameViewModel : ViewModel() {

    private var logMsg = ""
    private var actionID = "none"
    private lateinit var messages: ArrayList<GameMessage>
    val gameMessages = MutableLiveData<List<GameMessage>>()


    fun initViews(
        b: FragmentGameBinding,
        context: Context, //TODO: Context inside a ViewModel is bad
        numPlayers: Int,
    ) {
        messages = GameMessage.getGameActionsList()
        val adapter = RecyclerViewLogAdapter(messages)
        b.rvLog.adapter = adapter
        b.rvLog.layoutManager = LinearLayoutManager(context)

        Game.buttons = listOf(
            b.btn1, b.btn2, b.btn3, b.btn4, b.btn5, b.btn6,
            b.btn7, b.btn8, b.btn9, b.btn10, b.btn11, b.btn12
        )
        val points = generatePivotPoints(numPlayers)

        for (i in 12 - 1 downTo Game.numPlayers) {
            Game.buttons[i].visibility = View.GONE
            logMsg += "Btn '${i + 1}' hidden\n"
        }
        for (i in 0 until numPlayers) {
            val КОСТЫЛЬ999 = 60 //TODO: Убрать костыль
            Game.buttons[i].x = points[i][0].toFloat() + b.cvTableContainer.x
            Game.buttons[i].y = points[i][1].toFloat() + b.cvTableContainer.y + КОСТЫЛЬ999

            Game.buttons[i].setOnClickListener { v ->
                lateinit var output: IntArray
                when (actionID) {
                    "kill" -> {
                        output = CmdManager.commit(KillAction(i))
                        v.isEnabled = false
                    }

                    "vote" -> {
                        output = CmdManager.commit(AddToVoteAction(i, 5))
                    }

                    "foul" -> {
                        output = CmdManager.commit(FoulAction(i))
                        foulTV(output[2], b)
                    }

                    else -> {
                        output = IntArray(3)
                    }
                }
                controlUndoRedo(output, b, adapter)
                actionID = "none"
                if (!Game.gameActive) gameEndTV(b)
            }
        }



        Log.d("GraphLog", logMsg); logMsg = ""

        b.rightSideBar.btnKill.setOnClickListener { actionID = "kill" }

        b.rightSideBar.btnVote.setOnClickListener { actionID = "vote" }

        b.rightSideBar.btnFoul.setOnClickListener { actionID = "foul" }

        b.rightSideBar.btnDonCheck.setOnClickListener { actionID = "cdon" }

        b.rightSideBar.btnShrCheck.setOnClickListener { actionID = "cshr" }

        b.btnPeep.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                for (i in 0 until numPlayers)
                    Game.buttons[i].text = Game.players[i].emoji
                view.performClick()
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                for (i in 0 until numPlayers)
                    Game.buttons[i].text = (i + 1).toString()
                view.performClick()
            }
            false
        }

        b.btnUndo.setOnClickListener { controlUndoRedo(CmdManager.undo(), b,adapter) }

        b.btnRedo.setOnClickListener { controlUndoRedo(CmdManager.redo(), b,adapter) }

        //debug buttons
        b.btnDState.setOnClickListener { Game.printState() }
        b.btnDTest.text = "update logs"
        b.btnDTest.setOnClickListener { adapter.updateMessagesList() }


    }

    val blinkDur = 2_000
    fun foulTV(id: Int, b: FragmentGameBinding) {
        b.tvBig.text = Game.players[id].fouls.toString()
        b.tvUpper.text = "Player #${Game.players[id].strNum} fouls: "
        blink(blinkDur, b)
    }

    fun gameEndTV(b: FragmentGameBinding) {
        b.tvBig.text = ""
        b.tvUpper.text = "Game over"
        blink(blinkDur, b)
    }

    fun blink(dur: Int, b: FragmentGameBinding) {
        val appearDuration = dur / 4
        val disappearDuration = dur / 4
        b.tvBig.alpha = 0f
        b.tvBig.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    b.tvBig.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                b.tvBig.alpha = 0f
                            }
                        })
                }
            })

        b.tvUpper.alpha = 0f
        b.tvUpper.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    b.tvUpper.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                b.tvUpper.alpha = 0f
                            }
                        })
                }
            })


    }
}

private fun controlUndoRedo(arr: IntArray, b: FragmentGameBinding, adapter:RecyclerViewLogAdapter) {
    when (arr[0]) {
        -1 -> b.btnUndo.isEnabled = false
        1 -> b.btnUndo.isEnabled = true
        else -> {}
    }
    when (arr[1]) {
        -1 -> b.btnRedo.isEnabled = false
        1 -> b.btnRedo.isEnabled = true
        else -> {}
    }
    adapter.updateMessagesList() //TODO: УБРАТЬ GOVNOCODE
}

private fun generatePivotPoints(
    numPlayers: Int,
    radius: Int = 330,
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