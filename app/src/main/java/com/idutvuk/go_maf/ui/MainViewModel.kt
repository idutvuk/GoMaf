package com.idutvuk.go_maf.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.model.gamedata.CmdCommitType
import com.idutvuk.go_maf.model.GameManager
import com.idutvuk.go_maf.model.database.GamesRepository
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.MafiaGamesDatabase
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.MainBtnState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : ViewModel() {
    val allGames: LiveData<List<MafiaGame>>
    private var repository: GamesRepository
    val searchResults: MutableLiveData<List<MafiaGame>>
    private lateinit var manager: GameManager

    private val _uiState = MutableStateFlow(MafiaGameState(10))
    val uiState: StateFlow<MafiaGameState> = _uiState.asStateFlow()

    init {
        val gamesDb = MafiaGamesDatabase.getDatabase(application)
        val gameDao = gamesDb.gameDao()
        repository = GamesRepository(gameDao)

        allGames = repository.allGames
        searchResults = repository.searchResults


    }
    fun insertGame(game: MafiaGame) {
        repository.insertGame(game)
    }

    fun findGame(id: Int) {
        repository.getGame(id)
    }

    fun startGame(playerCount: Int) {
        manager = GameManager(playerCount)
    }

    fun clickButton(index: Int) {
        if (_uiState.value.mainBtnState == MainBtnState.WAITING_FOR_CLICK) {
            switchSelection(index)
            manager.stateHistory[manager.currentHistoryIndex] = _uiState.value

            _uiState.value = manager.commit(CmdCommitType.PRESS_PLAYER_NUMBER)
        } else {
            when (_uiState.value.selectionMode) {
                PlayerSelectionMode.NONE -> return
                PlayerSelectionMode.MULTIPLE -> switchSelection(index)
                PlayerSelectionMode.SINGLE -> {
                    _uiState.value.selectedPlayers.clear()
                    _uiState.value.selectedPlayers.add(index)
                }
            }
        }
    }

    private fun switchSelection(numberToSwitch: Int) {
        if (_uiState.value.selectedPlayers.contains(numberToSwitch)) {
            _uiState.value.selectedPlayers.remove(numberToSwitch)
        } else {
            _uiState.value.selectedPlayers.add(numberToSwitch)
        }
    }

    fun commit() {
        _uiState.value = manager.commit(CmdCommitType.PRESS_MAIN_BTN)
    }



    fun onPressUndoBtn() {
        manager.stateHistory.removeLastOrNull()
        _uiState.value = manager.stateHistory.last()
    }
}