package com.idutvuk.go_maf.ui.game


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.model.CmdCommitType
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.Player
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode


class GameViewModel : ViewModel() {

    private lateinit var messages: ArrayList<GameMessage>
    val gameMessages = MutableLiveData<List<GameMessage>>()

    //Объявляю LiveData, содержащую в себе Int внутри ViewModel
    private var gameState: MafiaGameState = MafiaGameState()

    var nldSelectionMode: PlayerSelectionMode = PlayerSelectionMode.NONE //TODO: name it good but not "notLiveData..."

    val ldTime = MutableLiveData(GameTime.NIGHT)
    val ldButtonsSelected = MutableLiveData(Array(Game.numPlayers) {false})
    val ldPlayersVis = MutableLiveData(Array(Game.numPlayers) { true })
    val ldMainButtonState = MutableLiveData(MainButtonActionState.DEBUG)
    val ldMainButtonOverwriteString: MutableLiveData<String> = MutableLiveData(null)
    val ldBackButton = MutableLiveData(false)
    val ldSkipButton = MutableLiveData(false)
    val ldVoteList = MutableLiveData<Set<Player>>()
    val ldHeading = MutableLiveData("Def heading")
    val ldDescription = MutableLiveData("Def text")
    val ldTimerActive = MutableLiveData(false)
    val ldCursor = MutableLiveData(0)



    /**
     * Main onclick performer from main button
     * TODO: implement 2-click logic & foul logic
     */
    fun onClickBtnMain() {
//        gameState = CmdManager.pressMainBtn(gameState.mainButtonActionState)
        gameState = CmdManager.commit(CmdCommitType.PRESS_MAIN_BTN)
        updateUiParams()
    }
    private fun updateUiParams() {
        with(gameState) {
            ldTime.value = time
            ldPlayersVis.value = Array(Game.numPlayers, init = {players[it].isEnabled})
            ldButtonsSelected.value = Array(Game.numPlayers, init = {selectedPlayers.contains(it)})
            ldMainButtonState.value = mainButtonActionState
            ldMainButtonOverwriteString.value = mainButtonOverwriteString
            ldBackButton.value = false //TODO: implement
            ldSkipButton.value = false //TODO: implement
            ldVoteList.value = voteList
            ldHeading.value = headingText
            ldDescription.value = descriptionText
            ldTimerActive.value = isTimerActive
            ldCursor.value = cursor

            nldSelectionMode = selectionMode
        }
    }

    fun performPlayerBtnClick(clickedIndex: Int, selectionState: Boolean) {
        if (gameState.mainButtonActionState == MainButtonActionState.WAITING_FOR_CLICK) {
            gameState.selectedPlayers.add(clickedIndex)
            gameState = CmdManager.commit(CmdCommitType.PRESS_PLAYER_NUMBER)
            updateUiParams()
            return
        }

        if (nldSelectionMode == PlayerSelectionMode.NONE) return

        if (selectionState) {
            gameState.selectedPlayers.remove(clickedIndex)
        } else {
            if (nldSelectionMode == PlayerSelectionMode.SINGLE) {
                gameState.selectedPlayers = arrayListOf(clickedIndex)
            } else {
                gameState.selectedPlayers.add(clickedIndex)
            }
        }

        updateUiParams()
    }

    fun skipNight() {
        gameState = CmdManager.commit(CmdCommitType.SKIP_NIGHT)
        updateUiParams()
    }

    fun skipDay() {
        gameState = CmdManager.commit(CmdCommitType.SKIP_DAY)
        updateUiParams()
    }

    fun getEmoji(i: Int): CharSequence {
        return gameState.players[i].emoji
    }

}



