package com.idutvuk.go_maf.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.relation.MafiaGameWithPlayers
import java.sql.Date

@Dao
@TypeConverters(Converters::class)
interface GameDao {
    @Query("SELECT * FROM game ORDER BY start_date ASC")
    fun getAllGames(): LiveData<List<MafiaGame>>
    @Query("SELECT * FROM game WHERE id = :id")
    fun findGameById(id: Int): List<MafiaGame>

    @Query("SELECT * FROM game WHERE start_date BETWEEN :dateStart AND :dateEnd")
    fun findGamesByDateRange(dateStart: Date, dateEnd: Date): List<MafiaGame>
    @Insert
    fun insertAllGames(vararg games: MafiaGame)

    @Query("DELETE FROM game WHERE id = :id")
    fun deleteGameById(id: Int)

    @Transaction
    @Query("SELECT * FROM game WHERE id = :gameId")
    fun getGameWithPlayers(gameId: Long): LiveData<MafiaGameWithPlayers>
}