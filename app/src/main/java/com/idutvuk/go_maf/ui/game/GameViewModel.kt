package com.idutvuk.go_maf.ui.game


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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


    val ldTime = MutableLiveData(GameTime.NIGHT)
    val ldPlayersVis = MutableLiveData(Array(Game.numPlayers) { true })
    val ldMainButtonState = MutableLiveData(ActionState.DEBUG)
    val ldMainButtonOverwriteString: MutableLiveData<String> = MutableLiveData(null)
    val ldBackButton = MutableLiveData(false)
    val ldSkipButton = MutableLiveData(false)
    val ldVoteList = MutableLiveData<List<Player>>()
    val ldHeading = MutableLiveData("Def heading")
    val ldDescription = MutableLiveData("Def text")
    val ldTimerActive = MutableLiveData(false)
    val ldCursor = MutableLiveData(0)



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

        gameState = CmdManager.pressMainBtn(gameState.actionState)
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
            ldCursor.value = cursor
        }
    }

    fun getEmoji(i: Int): CharSequence {
        return gameState.players[i].emoji
    }


}



