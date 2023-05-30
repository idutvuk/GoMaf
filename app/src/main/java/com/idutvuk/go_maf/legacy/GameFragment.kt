package com.idutvuk.go_maf.legacy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.gamedata.GameTime
import kotlin.math.cos
import kotlin.math.sin

@Deprecated("Use compose")
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var b: FragmentGameBinding

    private val angles = generatePlayerAngles(Game.numPlayers)
    private val pivotPoints = generatePivotPoints(angles)

    private val buttons = mutableListOf<MaterialButton>()
    private var lastAngle: Float = -1F

    private var isFabFoulPressed: MutableLiveData<Boolean> = MutableLiveData(false) // if pressed, next click on the player add foul to him

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)


        //setup RecyclerView adapter
        val adapter = RecyclerViewLogAdapter(ArrayList())
        b.bottomSheetLayout.rvLog.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        b.bottomSheetLayout.rvLog.layoutManager = layoutManager



        // create a layout params object for the buttons
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        for (i in 0 until Game.numPlayers) {
            buttons.add(MaterialButton(requireContext(), null, R.attr.playerButtonStyle))
            buttons[i].text = "  $i  " //TODO: replace to (i + 1)

            layoutParams.apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }

            buttons[i].layoutParams = layoutParams

            buttons[i].setOnClickListener {
                if (isFabFoulPressed.value!!) {
                    viewModel.performFoul(i)
                    isFabFoulPressed.value = false
                } else {
                    viewModel.performPlayerBtnClick(i)
                    Log.d("GraphLog", "Clicked $i")
                }

            }

            // add the button to the layout
            b.clContainer.addView(buttons[i], layoutParams)
            buttons[i].x += pivotPoints[i][0]
            buttons[i].y += pivotPoints[i][1]
        }

//subscribers

        viewModel.ldTimerActive.observe(viewLifecycleOwner) {
            b.bottomSheetLayout.btnAddTime.isEnabled = it
            b.bottomSheetLayout.btnPause.isEnabled = it
            if (it) {
                TimerHandler.startTimer(b.table.tvTimer, b.table.pbTimer, 60000)
                //TODO: make not-only 60s timers
            } else {
                TimerHandler.skipTimer(b.table.tvTimer, b.table.pbTimer)
                //TODO: make invisible TV
            }
        }

        viewModel.ldTime.observe(viewLifecycleOwner) {
            b.bottomSheetLayout.btnPrevPhase.icon = ContextCompat.getDrawable(requireContext(),
                if (it == GameTime.DAY) GameTime.DAY.icon
                else GameTime.NIGHT.icon
            )
            b.bottomSheetLayout.btnNextPhase.icon = ContextCompat.getDrawable(requireContext(),
                if (it == GameTime.DAY) GameTime.NIGHT.icon
            else GameTime.DAY.icon
            )
        }

       viewModel.ldHeading.observe(viewLifecycleOwner) {
           b.tvHeadline.text = it
       }

        viewModel.ldDescription.observe(viewLifecycleOwner) {
            b.tvDescription.text = it
        }

        viewModel.ldVoteList.observe(viewLifecycleOwner) {

            b.table.tvContextInfo.text = it
        }

        viewModel.ldMainButtonState.observe(viewLifecycleOwner) {
            Log.d("GameLog", "(GameFragment) main button changed in the UI to the $it")
            with(b.bottomSheetLayout.btnMain) {
                text = it.text
                setCompoundDrawablesWithIntrinsicBounds(it.icon, 0, 0, 0)
            }
        }

        viewModel.ldPlayersVis.observe(viewLifecycleOwner) {
            for (i in 0 until Game.numPlayers) {
                buttons[i].isEnabled = it[i]
            }
        }

        viewModel.ldButtonsSelected.observe(viewLifecycleOwner) {
            for(i in 0 until Game.numPlayers) {
                buttons[i].strokeWidth =  if (it[i]) 3 else 0
            }
            adapter.updateMessagesList() //todo move it to other part of the code (combine all the livedata?)
            b.bottomSheetLayout.rvLog.scrollToPosition(0)
        }

        viewModel.ldCursor.observe(viewLifecycleOwner) {
            pointArrowOnPlayer(it)
        }

        //setup bottom sheet behavior
        BottomSheetBehavior.from(b.bottomSheetLayout.bottomSheet).apply {
            peekHeight = 380
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }


        isFabFoulPressed.observe(viewLifecycleOwner) {
            if(it) {
                b.fabFoul.setImageResource(R.drawable.ic_close)
            }
            else {
                b.fabFoul.setImageResource(R.drawable.ic_foul)
            }
        }

        b.fabFoul.setOnClickListener {
            isFabFoulPressed.value = !isFabFoulPressed.value!!
        }

        b.fabPeep.setOnClickListener {
            if (buttons[0].text != "  0  ") {
                for (i in 0 until Game.numPlayers)
                    buttons[i].text = "  $i  " //TODO: replace to (i + 1)
            } else {
                for (i in 0 until Game.numPlayers)
                    buttons[i].text = " ${viewModel.getEmoji(i)} "
            }
        }



        //TODO: uncomment
//        b.bottomSheetLayout.btnUndo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.undo(), b,adapter) }

//        b.bottomSheetLayout.btnRedo.setOnClickListener { viewModel.controlUndoRedo(CmdManager.redo(), b,adapter) }

        b.bottomSheetLayout.btnNextPhase.setOnClickListener {
            if (viewModel.ldTime.value == GameTime.DAY) { //if pressed on day
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Are you really want to skip day?")
                    .setMessage("This will skip:\nPlayer speeches\nVote")
                    .setPositiveButton("Skip") { _, which ->
                        viewModel.skipDay()
                    }
                    .setNegativeButton("Cancel") {dialog, which ->
                    }
                    .show()
            } else { //if pressed on night
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Are you really want to skip night?")
                    .setMessage("This will skip:\nMafia mafiaKill\nDon check\nSheriff check\nBest move")
                    .setPositiveButton("Skip") { _, which ->
                        viewModel.skipNight()
                    }
                    .setNegativeButton("Cancel") {dialog, which ->
                    }
                    .show()
            }
        }

        viewModel.ldSnackbarMessage.observe(viewLifecycleOwner) {
            Snackbar.make(b.topAppBar,it,Snackbar.LENGTH_SHORT).show()
        }

        b.bottomSheetLayout.btnPause.setOnClickListener {
            if (TimerHandler.isRunning) {
                TimerHandler.pauseTimer()
                b.bottomSheetLayout.btnPause.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_play)
            } else {
                TimerHandler.resumeTimer(b.table.tvTimer, b.table.pbTimer)
                b.bottomSheetLayout.btnPause.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause)
            }
        }

        b.bottomSheetLayout.btnAddTime.setOnClickListener {
            TimerHandler.addTime(b.table.tvTimer, b.table.pbTimer, 5000)
        }

        b.bottomSheetLayout.btnMain.setOnClickListener {
            isFabFoulPressed.value = false
            viewModel.onClickBtnMain()
        }

        b.fabDebug.setOnClickListener {

        }

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


private fun generatePivotPoints(angles: ArrayList<Float>, radius: Int = 390): Array<IntArray> { //TODO: change to dynamic radius
    val numPlayers = angles.size
    val pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }
    for (i in 0 until numPlayers) {
        val x = -(radius * sin(angles[i])).toInt()
        val y = (radius * cos(angles[i])).toInt()
        pivotPoints[i] = intArrayOf(x, y)
    }
    return pivotPoints
}

