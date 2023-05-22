package com.idutvuk.go_maf.model.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*

class GamesRepository(private val gameDao: GameDao) {
    val allGames: LiveData<List<MafiaGame>> = gameDao.getAll()
    val searchResults = MutableLiveData<List<MafiaGame>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun insertGame(newGame: MafiaGame) {
        coroutineScope.launch(Dispatchers.IO) {
            gameDao.insertAll(newGame)
        }
    }

    fun getGame(id: Int) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(id).await()
        }
    }

    fun deleteGame(id: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            gameDao.delete(id)
        }
    }

    private fun asyncFind(id: Int): Deferred<List<MafiaGame>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async gameDao.getItem(id)
        }

    companion object {
        const val IS_DATA_MOCKED = false
    }
}