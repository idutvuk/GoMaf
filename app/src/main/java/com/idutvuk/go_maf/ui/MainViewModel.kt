package com.idutvuk.go_maf.ui

import android.app.Application
import android.util.Log
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
import com.idutvuk.go_maf.ui.game.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : ViewModel() {
    val allGames: LiveData<List<MafiaGame>>
    private var repository: GamesRepository
    val searchResults: MutableLiveData<List<MafiaGame>>
    private var manager: GameManager
    private lateinit var gameState: MafiaGameState

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        val gamesDb = MafiaGamesDatabase.getDatabase(application)
        val gameDao = gamesDb.gameDao()
        repository = GamesRepository(gameDao)

        allGames = repository.allGames
        searchResults = repository.searchResults
        manager = GameManager(10)

    }
    fun insertGame(game: MafiaGame) {
        repository.insertGame(game)
    }

    fun findGame(id: Int) {
        repository.getGame(id)
    }

    fun commit() {
        gameState = manager.commit(CmdCommitType.PRESS_MAIN_BTN)
        Log.d("GameLog", gameState.mainBtnState.toString())
        _uiState.value = GameUiState(gameState.mainBtnState)
    }
}