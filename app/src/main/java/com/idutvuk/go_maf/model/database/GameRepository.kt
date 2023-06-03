package com.idutvuk.go_maf.model.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.relation.MafiaGameWithPlayers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*

class GameRepository(private val gameDao: GameDao) {
    val allGames: LiveData<List<MafiaGame>> = gameDao.getAllGames()
    val searchResults = MutableLiveData<List<MafiaGame>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertGame(newGame: MafiaGame): List<Long> {
//        withContext(Dispatchers.IO) {
            return gameDao.insertGames(newGame)
//        }
    }

    fun getGame(id: Long) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(id).await()
        }
    }

    fun deleteGame(id: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            gameDao.deleteGameById(id)
        }
    }

    fun getGameWithPlayers(gameId: Long): LiveData<MafiaGameWithPlayers> {
        return gameDao.getGameWithPlayers(gameId)
    }

    fun finishGame(gameId: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            gameDao.finishGame(
                gameId,  System.currentTimeMillis() - gameDao.findGameById(gameId)[0].startTime
            )
        }
    }

    private fun asyncFind(id: Long): Deferred<List<MafiaGame>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async gameDao.findGameById(id)
        }

    companion object {
        const val IS_DATA_MOCKED = false
    }
}