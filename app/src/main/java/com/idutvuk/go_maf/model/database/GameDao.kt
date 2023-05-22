package com.idutvuk.go_maf.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface GameDao {

//    @Query("SELECT * FROM games ORDER BY date ASC")
//    fun getAll(): Flow<List<MafiaGame>>
//
//    @Query("SELECT * FROM games WHERE id = :id")
//    fun getItem(id: Int): Flow<MafiaGame>
//
//    @Query("SELECT * FROM games WHERE date BETWEEN :dateStart AND :dateEnd")
//    fun findByDate(dateStart: Date, dateEnd: Date): Flow<List<MafiaGame>>
//    @Insert
//    fun insertAll(vararg games: MafiaGame)
//
//    @Delete
//    fun delete(game: MafiaGame)

    @Query("SELECT * FROM games ORDER BY date ASC")
    fun getAll(): LiveData<List<MafiaGame>>
    @Query("SELECT * FROM games WHERE id = :id")
    fun getItem(id: Int): List<MafiaGame>

    @Query("SELECT * FROM games WHERE date BETWEEN :dateStart AND :dateEnd")
    fun findByDate(dateStart: Date, dateEnd: Date): List<MafiaGame>
    @Insert
    fun insertAll(vararg games: MafiaGame)

    @Query("DELETE FROM games WHERE id = :id")
    fun delete(id: Int)
}