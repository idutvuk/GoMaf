package com.idutvuk.go_maf.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.gamedata.Game

import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.ui.TimerHandler
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var b: FragmentGameBinding

    private val angles = generatePlayerAngles(Game.numPlayers)
    private val pivotPoints = generatePivotPoints(angles)

    private val buttons = mutableListOf<MaterialButton>()
    private var lastAngle: Float = -1F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)


        // create a layout params object for the buttons
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        //TODO: fix oval shadow
        for (i in 0 until Game.numPlayers) {
            buttons.add(MaterialButton(requireContext(), null, R.attr.playerButtonStyle))
            buttons[i].text = (i).toString() //TODO: replace to (i + 1)

            layoutParams.apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }

            buttons[i].layoutParams = layoutParams

            buttons[i].setOnClickListener {
                viewModel.performPlayerBtnClick(i,viewModel.ldButtonsSelected.value!![i])
                Log.d("GraphLog", "Clicked $i")
            }

            // add the button to the layout
            b.clContainer.addView(buttons[i], layoutParams)
            buttons[i].x += pivotPoints[i][0]
            buttons[i].y += pivotPoints[i][1]
        }


//        val messages = GameMessage.getGameActionsList()
        val messages = GameMessage.createGameMessagesList(10)
        val adapter = RecyclerViewLogAdapter(messages)


//subscribers

        viewModel.ldTimerActive.observe(viewLifecycleOwner) {
            b.fabAdd.isEnabled = it
            b.fabPause.isEnabled = it
            if (it) {
                TimerHandler.startTimer(b.table.tvTimer, b.table.pbTimer, 60000)
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
            )
        }

       viewModel.ldHeading.observe(viewLifecycleOwner) {
           b.tvHeadline.text = it
       }

        viewModel.ldVoteList.observe(viewLifecycleOwner) {
            var shortVoteList = "${viewModel.nldSelectionMode}"
            for (element in it) {
                shortVoteList += element.number.toString() + ", "
            }
            b.table.tvContextInfo.text = shortVoteList
        }

        viewModel.ldMainButtonState.observe(viewLifecycleOwner) {
            Log.d("GameLog", "(GameFragment) main button changed in the UI to the $it")
            with(b.bottomSheetLayout.btnMain) {
                val tmp = viewModel.ldMainButtonOverwriteString.value
                text = if (!tmp.isNullOrEmpty()) it.text + tmp else it.text
                viewModel.ldMainButtonOverwriteString.value = ""
                setCompoundDrawablesWithIntrinsicBounds(it.icon, 0, 0, 0)
            }
        }

        viewModel.ldPlayersVis.observe(viewLifecycleOwner) {
            //TODO: fix it so it wouldn't restart every time
            Log.d("GraphLog", "Player visibility changed")
            for (i in 0 until Game.numPlayers) {
                buttons[i].isEnabled = it[i]
            }
        }

        viewModel.ldButtonsSelected.observe(viewLifecycleOwner) {
            for(i in 0 until Game.numPlayers) {
                buttons[i].strokeWidth =  if (it[i]) 3 else 0
            }
        }

        viewModel.ldCursor.observe(viewLifecycleOwner) {
            pointArrowOnPlayer(it)
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
                    buttons[i].text = viewModel.getEmoji(i)
                view.performClick()
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                for (i in 0 until Game.numPlayers)
                    buttons[i].text = (i).toString() //TODO: replcae to (i + 1)
                view.performClick()
            }
            false
        }


        //TODO: uncomment
//        b.bottomSheetLayout.btnUndo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.undo(), b,adapter) }

//        b.bottomSheetLayout.btnRedo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.redo(), b,adapter) }


        b.table.fabPause.setOnClickListener {
            if (TimerHandler.isRunning) {
                TimerHandler.pauseTimer()
                b.table.fabPause.setImageResource(R.drawable.ic_play)
            } else {
                TimerHandler.resumeTimer(b.table.tvTimer, b.table.pbTimer)
                b.table.fabPause.setImageResource(R.drawable.ic_pause)
            }
        }

        b.table.fabAdd.setOnClickListener {
            TimerHandler.addTime(b.table.tvTimer, b.table.pbTimer, 5000)
        }

        b.bottomSheetLayout.btnMain.setOnClickListener {
            viewModel.onClickBtnMain()
        }

        b.fabDebug.setOnClickListener {
            pointArrowOnPlayer(7)
        }

        //TODO: make GameFragment readable

        return b.root
    }

    /**
     * just give this method a number of player to point at
     */
    private fun pointArrowOnPlayer(playerNumber: Int) {
        val newAngle = Math.toDegrees(angles[playerNumber].toDouble()).toFloat() + 180F
        val pivotX: Float = (b.table.ivArrow.width / 2).toFloat()
        val pivotY: Float = (b.table.ivArrow.height / 2).toFloat()
        val animation: Animation =
            RotateAnimation(if (lastAngle == -1F) 180F else lastAngle, newAngle, pivotX, pivotY)
        lastAngle = newAngle
        animation.duration = 500
        animation.fillAfter = true
        b.table.ivArrow.startAnimation(animation)
    }

}

/**
 * generates some angles in radians
 */
private fun generatePlayerAngles(numPlayers: Int): ArrayList<Float> {
    val angles: ArrayList<Float> = ArrayList(numPlayers)
    val angleOffset = Math.toRadians(60.0)
    for (i in 0 until numPlayers) {
        angles.add(((2 * Math.PI - angleOffset) / (numPlayers - 1) * i + angleOffset / 2).toFloat())
    }
    return angles
}


private fun generatePivotPoints(angles: ArrayList<Float>, radius: Int = 330): Array<IntArray> {
    val numPlayers = angles.size
    val pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }
    for (i in 0 until numPlayers) {
        val x = -(radius * sin(angles[i])).toInt()
        val y = (radius * cos(angles[i])).toInt()
        pivotPoints[i] = intArrayOf(x, y)
    }
    return pivotPoints
}


