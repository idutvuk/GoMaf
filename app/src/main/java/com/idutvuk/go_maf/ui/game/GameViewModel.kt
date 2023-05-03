package com.idutvuk.go_maf.ui.game


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player


class GameViewModel : ViewModel() {

    private lateinit var messages: ArrayList<GameMessage>
    val gameMessages = MutableLiveData<List<GameMessage>>()

    //Объявляю LiveData, содержащую в себе Int внутри ViewModel
    private var gameState: MafiaGameState = MafiaGameState()

    //Add ui params
    //TODO: connect them to the ui
    val ldTime = MutableLiveData(GameTime.NIGHT)
    val ldPlayersVis = MutableLiveData(Array(Game.numPlayers) { true }) //TODO: change 10 to player count
    val ldMainButtonState = MutableLiveData(ActionState.DEBUG)
    val ldMainButtonOverwriteString: MutableLiveData<String> = MutableLiveData(null)
    val ldBackButton = MutableLiveData(false)
    val ldSkipButton = MutableLiveData(false)
    val ldVoteList = MutableLiveData<List<Player>>()
    val ldHeading = MutableLiveData("Def heading")
    val ldDescription = MutableLiveData("Def text")
    val ldTimerActive = MutableLiveData(false)



    private val blinkDur = 2_000
    fun foulTV(id: Int, b: FragmentGameBinding) {
        b.table.tvBig.text = Game.players[id].fouls.toString()
        b.table.tvUpper.text = "Player #${Game.players[id].strNum} fouls: "
        blink(blinkDur, b)
    }

    fun gameEndTV(b: FragmentGameBinding) {
        b.table.tvBig.text = ""
        b.table.tvUpper.text = "Game over"
        blink(blinkDur, b)
    }

    fun blink(dur: Int, b: FragmentGameBinding) {
        val appearDuration = dur / 4
        val disappearDuration = dur / 4
        b.table.tvBig.alpha = 0f
        b.table.tvBig.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    b.table.tvBig.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                b.table.tvBig.alpha = 0f
                            }
                        })
                }
            })

        b.table.tvUpper.alpha = 0f
        b.table.tvUpper.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    b.table.tvUpper.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                b.table.tvUpper.alpha = 0f
                            }
                        })
                }
            })


    }
    fun controlUndoRedo(arr: IntArray, b: FragmentGameBinding, adapter: RecyclerViewLogAdapter) {
        when (arr[0]) {
            -1 -> b.bottomSheetLayout.btnUndo.isEnabled = false
            1 -> b.bottomSheetLayout.btnUndo.isEnabled = true
            else -> {}
        }
        when (arr[1]) {
            -1 -> b.bottomSheetLayout.btnRedo.isEnabled = false
            1 -> b.bottomSheetLayout.btnRedo.isEnabled = true
            else -> {}
        }
        adapter.updateMessagesList() //TODO: УБРАТЬ GOVNOCODE
    }

    /**
     * Main onclick performer from main button
     * TODO: implement 2-click logic & foul logic
     */
    fun onClickBtnMain() {
        if (gameState.actionState.requireNumber) {
            //TODO: w/number logic
        } else {
            //TODO: single-tap logic (if needed)
        }

        gameState = CmdManager.commit(gameState.actionState)
        updateUiParams(gameState)
    }
    private fun updateUiParams(gameState: MafiaGameState) {
        with(gameState) {
            ldTime.value = time
            ldPlayersVis.value = Array(Game.numPlayers, init = {players[it].isEnabled})
            ldMainButtonState.value = actionState
            ldMainButtonOverwriteString.value = mainButtonOverwriteString
            ldBackButton.value = false //TODO: implement
            ldSkipButton.value = false //TODO: implement
            ldVoteList.value = voteList
            ldHeading.value = headingText
            ldDescription.value = descriptionText
            ldTimerActive.value = isTimerActive
        }
    }

    fun onClickFab1Debug() {
        Log.d("GameLog","(from debug fab1 in GVM):\nCurrent game state:\n" +
                gameState.toString())
    }

    fun onClickFab2Debug() {
        Log.d("GameLog","(from debug fab2 in GVM):\nCurrent game state history:\n" +
                CmdManager.stateHistory.toString())
    }
}



