package com.idutvuk.go_maf.legacy


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.model.gamedata.CmdCommitType
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import com.idutvuk.go_maf.model.gamedata.MainBtnState

@Deprecated("Use compose")
class GameViewModel : ViewModel() {

    private lateinit var messages: ArrayList<GameMessage>
    val gameMessages = MutableLiveData<List<GameMessage>>()

    //Объявляю LiveData, содержащую в себе Int внутри ViewModel
    private lateinit var gameState: MafiaGameState

    var selectionMode: PlayerSelectionMode = PlayerSelectionMode.NONE //TODO: name it good but not "notLiveData..."

    val ldTime = MutableLiveData(GameTime.NIGHT)
    val ldButtonsSelected = MutableLiveData(Array(Game.numPlayers) {false})
    val ldPlayersVis = MutableLiveData(Array(Game.numPlayers) { true })
    val ldMainButtonState = MutableLiveData(MainBtnState.DEBUG)
    val ldBackButton = MutableLiveData(false)
    val ldSkipButton = MutableLiveData(false)
    val ldVoteList = MutableLiveData<String>()
    val ldHeading = MutableLiveData("Def heading")
    val ldDescription = MutableLiveData("Def text")
    val ldTimerActive = MutableLiveData(false)
    val ldCursor = MutableLiveData(0)
    val ldSnackbarMessage: MutableLiveData<String> = MutableLiveData(null)



    /**
     * Main onclick performer from main button
     * TODO: implement 2-click logic & foul logic
     */
    fun onClickBtnMain() {
//        gameState = CmdManager.pressMainBtn(gameState.mainBtnState)
        gameState = CmdManager.commit(CmdCommitType.PRESS_MAIN_BTN)
        updateUiParams()
    }
    private fun updateUiParams() {

        with(gameState) {
            var votelistString = ""
            for (i in voteList)
                votelistString += "$i, "
            ldVoteList.value = votelistString

            ldTime.value = time
            ldPlayersVis.value = Array(Game.numPlayers, init = {players[it].isEnabled})
            ldButtonsSelected.value = Array(Game.numPlayers, init = {selectedPlayers.contains(it)})
            ldMainButtonState.value = mainBtnState
            ldBackButton.value = false //TODO: implement
            ldSkipButton.value = false //TODO: implement
            ldHeading.value = primaryMessage
            ldDescription.value = secondaryMessage
            ldTimerActive.value = isTimerActive
            ldCursor.value = cursor
            snackbarMessage?.let { ldSnackbarMessage.value = it }

            this@GameViewModel.selectionMode = selectionMode
        }
    }

    fun performPlayerBtnClick(clickedIndex: Int) {

        if (gameState.mainBtnState == MainBtnState.WAITING_FOR_CLICK) {
            gameState.togglePlayerSelection(clickedIndex)
            gameState = CmdManager.commit(CmdCommitType.PRESS_PLAYER_NUMBER)
            updateUiParams()
            return
        }

        if (selectionMode == PlayerSelectionMode.NONE) return

        if (ldButtonsSelected.value!![clickedIndex]) {
            gameState.togglePlayerSelection(clickedIndex)
        } else {
            if (selectionMode == PlayerSelectionMode.SINGLE) gameState.clearSelection()
            gameState.togglePlayerSelection(clickedIndex)
        }

        updateUiParams()
    }
    fun performFoul(i: Int) {
        gameState.foul(i)
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
        return gameState.players[i].role.emoji
    }


}



