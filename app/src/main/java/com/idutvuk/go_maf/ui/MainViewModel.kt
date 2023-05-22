package com.idutvuk.go_maf.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.model.database.GamesRepository
import com.idutvuk.go_maf.model.database.MafiaGame
import com.idutvuk.go_maf.model.database.MafiaGamesDatabase

class MainViewModel(application: Application) : ViewModel() {
    val allGames: LiveData<List<MafiaGame>>
    private val repository: GamesRepository
    val searchResults: MutableLiveData<List<MafiaGame>>

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
}