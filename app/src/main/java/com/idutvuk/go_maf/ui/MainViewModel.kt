package com.idutvuk.go_maf.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.model.gamedata.CmdCommitType
import com.idutvuk.go_maf.model.GameManager
import com.idutvuk.go_maf.model.UserRepository
import com.idutvuk.go_maf.model.database.GameRepository
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.MafiaGamesDatabase
import com.idutvuk.go_maf.model.database.relation.MafiaGameWithPlayers
import com.idutvuk.go_maf.model.database.entities.User
import com.idutvuk.go_maf.model.gamedata.GameTime
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.model.gamedata.MainBtnState
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : ViewModel() {
    val allGames: LiveData<List<MafiaGame>>
    private var gameRepository: GameRepository
    private val userRepository: UserRepository
    val searchResults: MutableLiveData<List<MafiaGame>>
    private lateinit var manager: GameManager

    private lateinit var _uiState: MutableStateFlow<MafiaGameState>
    lateinit var uiState: StateFlow<MafiaGameState>

    init {
        val gamesDb = MafiaGamesDatabase.getDatabase(application)

        val gameDao = gamesDb.gameDao()
        gameRepository = GameRepository(gameDao)
        val userDao = gamesDb.userDao()
        userRepository = UserRepository(userDao)

        allGames = gameRepository.allGames
        searchResults = gameRepository.searchResults


    }
    fun insertGame(game: MafiaGame) {
        gameRepository.insertGame(game)
    }

    fun findGame(id: Int) {
        gameRepository.getGame(id)
    }

    fun getGameWithPlayers(id: Long): LiveData<MafiaGameWithPlayers> {
        return gameRepository.getGameWithPlayers(id)
    }

    fun getUser(userId: Long): User {
        return userRepository.getUser(userId)
    }

    suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }


    fun startGame(playerCount: Int) {
        manager = GameManager(playerCount)
        _uiState = MutableStateFlow(MafiaGameState(playerCount))
        uiState = _uiState.asStateFlow()
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
        manager.stateHistory[manager.currentHistoryIndex] = _uiState.value
        _uiState.value = manager.commit(CmdCommitType.PRESS_MAIN_BTN)
    }



    fun onPressUndoBtn() {
        _uiState.value = manager.undo()
    }

    fun nextPhase() {
        manager.stateHistory[manager.currentHistoryIndex] = _uiState.value
        _uiState.value = if (_uiState.value.time == GameTime.DAY) manager.commit(CmdCommitType.SKIP_DAY)
        else manager.commit(CmdCommitType.SKIP_NIGHT)
    }

    fun foul(index: Int) {
        _uiState.value.foul(index)
        manager.stateHistory[manager.currentHistoryIndex] = _uiState.value
    }

}