package com.idutvuk.go_maf.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.relation.MafiaGameWithPlayers
import kotlinx.coroutines.CompletableDeferred

@Dao
@TypeConverters(Converters::class)
interface GameDao {
    @Query("SELECT * FROM game ORDER BY start_time ASC")
    fun getAllGames(): LiveData<List<MafiaGame>>
    @Query("SELECT * FROM game WHERE id = :id")
    fun findGameById(id: Long): List<MafiaGame>



    @Insert
    fun insertGames(vararg games: MafiaGame): List<Long>

    @Query("DELETE FROM game WHERE id = :id")
    fun deleteGameById(id: Long)

    @Transaction
    @Query("SELECT * FROM game WHERE id = :gameId")
    fun getGameWithPlayers(gameId: Long): LiveData<MafiaGameWithPlayers>

    @Query("UPDATE game SET is_over = 1, duration = :duration WHERE id = :gameId")
    fun finishGame(gameId: Long, duration: Long)
}