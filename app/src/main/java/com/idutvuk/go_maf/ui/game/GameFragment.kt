package com.idutvuk.go_maf.ui.game

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.ui.TimerHandler
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var b: FragmentGameBinding
    private val buttons = mutableListOf<MaterialButton>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)

        val circlePoints = generateCirclePoints(Game.numPlayers, radius = 360)

        // create a layout params object for the buttons
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        //TODO: fix oval shadow
        for (i in 0 until Game.numPlayers)
        {
            // create a new button
            buttons.add(MaterialButton(requireContext(),null,R.attr.playerButtonStyle))

            // set the button's id
//            buttons.last().id = View.generateViewId()

            // set the button's text
            buttons.last().text = (i + 1).toString()

            // set the button's layout params

            layoutParams.apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }

            buttons.last().layoutParams = layoutParams

            // add the button to the layout
            b.clContainer.addView(buttons.last(), layoutParams)
            buttons.last().x += circlePoints[i][0]
            buttons.last().y += circlePoints[i][1]
        }





//        val messages = GameMessage.getGameActionsList()
        val messages = GameMessage.createGameMessagesList(10)
        val adapter = RecyclerViewLogAdapter(messages)


//subscribers

        viewModel.ldTimerActive.observe(viewLifecycleOwner) {
            b.fabAdd.isEnabled = it
            b.fabPause.isEnabled = it
            if (it) {
                TimerHandler.startTimer(b.table.tvTimer, b.table.pbTimer,60000 )
                //TODO: make not-only 60s timers
            } else {
                TimerHandler.skipTimer(b.table.tvTimer, b.table.pbTimer)
                //TODO: make invisible TV
            }
        }

        viewModel.ldTime.observe(viewLifecycleOwner) {
                b.ibDayTime.setImageResource(
                    if (it == GameTime.DAY) R.drawable.ic_sun
                    else R.drawable.ic_moon
                ) //TODO: fix the code (get id from the enum class
        }

        viewModel.ldMainButtonState.observe(viewLifecycleOwner) {
            Log.d("GameLog","(GameFragment) main button changed in the UI to the $it")
            with(b.bottomSheetLayout.btnMain) {
                val tmp = viewModel.ldMainButtonOverwriteString.value
                text = if (!tmp.isNullOrEmpty()) it.text + tmp else it.text
                viewModel.ldMainButtonOverwriteString.value = ""
                setCompoundDrawablesWithIntrinsicBounds(it.icon,0,0,0)
            }
        }

        viewModel.ldPlayersVis.observe(viewLifecycleOwner) {
            Log.d("GraphLog","Player visibility changed")
            for (i in 0 until Game.numPlayers) {
                buttons[i].isEnabled = it[i]
            }
        }


        //setup bottom sheet behavior
        BottomSheetBehavior.from(b.bottomSheetLayout.bottomSheet).apply {
            peekHeight = 400
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //setup RecyclerView adapter
        b.bottomSheetLayout.rvLog.adapter = adapter
        b.bottomSheetLayout.rvLog.layoutManager = LinearLayoutManager(context)


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

//        b.bottomSheetLayout.btnRedo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.redo(), b,adapter) }


        b.table.fabPause.setOnClickListener {
            if(TimerHandler.isRunning) {
                TimerHandler.pauseTimer()
                b.table.fabPause.setImageResource(R.drawable.ic_play)
            } else {
                TimerHandler.resumeTimer(b.table.tvTimer, b.table.pbTimer)
                b.table.fabPause.setImageResource(R.drawable.ic_pause)
            }
        }

        b.table.fabAdd.setOnClickListener{
            TimerHandler.addTime(b.table.tvTimer,b.table.pbTimer, 5000)
        }

        b.bottomSheetLayout.btnMain.setOnClickListener {
            viewModel.onClickBtnMain()
        }



        //TODO: make GameFragment readable

        return b.root
    }
}

private fun generateCirclePoints(
    numPlayers: Int,
    radius: Int = 330,
): Array<IntArray> {
    //TODO: create cursor
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