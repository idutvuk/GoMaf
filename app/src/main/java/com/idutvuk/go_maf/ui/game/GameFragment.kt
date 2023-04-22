package com.idutvuk.go_maf.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.GameMessage
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var b: FragmentGameBinding
    private lateinit var buttons: List<MaterialButton>
    private var actionId = ActionId.NONE
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)

        buttons = listOf(
            b.btn1, b.btn2, b.btn3, b.btn4, b.btn5, b.btn6,
            b.btn7, b.btn8, b.btn9, b.btn10, b.btn11, b.btn12
        )

        for (i in 12 - 1 downTo Game.numPlayers) {
            buttons[i].visibility = View.GONE
            Log.d("Graphlog","Btn '${i + 1}' hidden\n")
        }

//        val messages = GameMessage.getGameActionsList()
        val messages = GameMessage.createGameMessagesList(10)
        val adapter = RecyclerViewLogAdapter(messages)

        viewModel.initViews(b)




        viewModel.ldNumber.observe(viewLifecycleOwner) {
            b.tvDescription.text = "current: " + viewModel.ldNumber.value.toString()
            Log.d("GameLog", "observed")
        }


        BottomSheetBehavior.from(b.bottomSheetLayout.bottomSheet).apply {
            peekHeight = 400
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        b.bottomSheetLayout.rvLog.adapter = adapter
        b.bottomSheetLayout.rvLog.layoutManager = LinearLayoutManager(context)

//        b.bottomSheetLayout.rightSideBar.btnKill.setOnClickListener {
//            actionId = if (actionId == ActionId.KILL) ActionId.NONE else ActionId.KILL
//        }
//
//        b.bottomSheetLayout.rightSideBar.btnVote.setOnClickListener {
//            actionId = if (actionId == ActionId.VOTE) ActionId.NONE else ActionId.VOTE
//        }
//
//        b.bottomSheetLayout.rightSideBar.btnFoul.setOnClickListener {
//            actionId = if (actionId == ActionId.FOUL) ActionId.NONE else ActionId.FOUL
//        }
//
//        b.bottomSheetLayout.rightSideBar.btnShrCheck.setOnClickListener {
//            actionId = if (actionId == ActionId.CSHR) ActionId.NONE else ActionId.CSHR
//        }

//        b.rightSideBar.btnDonCheck.setOnClickListener {
//            actionId = if (actionId == ActionId.CDON) ActionId.NONE else ActionId.CDON
//        }


        val points = generatePivotPoints(Game.numPlayers) //TODO: replace numplayers with the another constant
        for (i in 0 until Game.numPlayers) {
            buttons[i].x += points[i][0].toFloat()
            buttons[i].y += points[i][1].toFloat()
        }
        for (i in 0 until Game.numPlayers) {
            buttons[i].setOnClickListener{
                lateinit var output: IntArray
                when (actionId) {
                    ActionId.KILL -> {
//                        output = CmdManager.commit(KillByMafiaAction(i))
                        it.isEnabled = false
                    }

                    ActionId.VOTE -> {
//                        output = CmdManager.commit(AddToVoteAction(i))
                    }

                    ActionId.FOUL -> {
//                        output = CmdManager.commit(FoulAction(i))
//                        viewModel.foulTV(output[2], b)
                    }

                    ActionId.CSHR -> {
//                        output = CmdManager.commit(CheckShrAction(i))
                    }

                    ActionId.CDON -> {
//                        output = CmdManager.commit(CheckDonAction(i))
                    }
                    ActionId.NONE -> {
//                        output = IntArray(3)
                    }
                }
                output = IntArray(3) //TODO: remove & uncomment
                viewModel.controlUndoRedo(output, b, adapter)
                actionId = ActionId.NONE
                if (!Game.gameActive) viewModel.gameEndTV(b) //TODO REMOVE AAAAAHh
            }
        }
        b.fabPeep.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                for (i in 0 until Game.numPlayers)
                    buttons[i].text = Game.players[i].emoji
                view.performClick()
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                for (i in 0 until Game.numPlayers)
                    buttons[i].text = (i + 1).toString()
                view.performClick()
            }
            false
        }


        b.bottomSheetLayout.btnUndo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.undo(), b,adapter) }

        b.bottomSheetLayout.btnRedo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.redo(), b,adapter) }


        var currentState = MainButtonState.START_GAME
        b.bottomSheetLayout.btnMain.setOnClickListener {
            currentState = when (currentState) {
                MainButtonState.START_GAME -> MainButtonState.START_DAY
                MainButtonState.START_DAY -> MainButtonState.START_VOTE
                MainButtonState.START_VOTE -> MainButtonState.START_NIGHT
                else -> MainButtonState.START_GAME
            }
            //TODO remove debug test implementation
            setMainButtonState(b.bottomSheetLayout.btnMain, currentState)
        }


        //TODO: implement binding interactions here instead of ViewModel
        return b.root
    }
}

private fun setMainButtonState(button: Button, currentState: MainButtonState) {
    button.text = currentState.text
    button.setCompoundDrawablesWithIntrinsicBounds(currentState.icon, 0, 0, 0)
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