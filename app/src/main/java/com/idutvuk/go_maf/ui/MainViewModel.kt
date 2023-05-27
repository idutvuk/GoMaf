package com.idutvuk.go_maf.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.model.CmdCommitType
import com.idutvuk.go_maf.model.GameManager
import com.idutvuk.go_maf.model.database.GamesRepository
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.MafiaGamesDatabase
import com.idutvuk.go_maf.model.gamedata.MafiaGameState

class MainViewModel(application: Application) : ViewModel() {
    val allGames: LiveData<List<MafiaGame>>
    private lateinit var repository: GamesRepository
    val searchResults: MutableLiveData<List<MafiaGame>>
    private lateinit var manager: GameManager
    private lateinit var gameState: MafiaGameState

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
    }
}