package com.idutvuk.go_maf.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.idutvuk.go_maf.model.database.MafiaAccount.Companion.users
import java.sql.Date
import java.sql.Time

@Entity(tableName = "game")
data class MafiaGame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="start_date")
    val startDate: Date,

    @ColumnInfo(name="duration")
    val duration: Int,

    @ColumnInfo(name = "is_over")
    val isOver: Boolean,

    @ColumnInfo(name = "num_players")
    val numPlayers: Int,

    @ColumnInfo(name = "host_user_id")
    val hostUserId: Int,
) {
    companion object {
        /**
         * mock data
         */
        val gameDates = (16409L..16638L).shuffled().take(20).map { it*100000L } // UNIX timestamps between 2022-01-01 and 2023-12-31
        val gameTimes = (0L..43200L).shuffled().take(20).map { Time(it * 1000) } // random times between 12:00 AM and 12:00 PM
        val numPlayers = (6..12).toList()
        val games by lazy {
            List(20) {
                val startTime = Time((gameDates[it] * 1000) + gameTimes[it].seconds)
                val duration = ((30..75).random() * 60)
                val players = users.shuffled().take(numPlayers.random()).toCollection(arrayListOf())
                MafiaGame(
                    startDate = Date(gameDates[it] * 1000),
                    duration = duration,
                    isOver = false,
                    numPlayers = players.size,
                    hostUserId = 1
                )
            }
        }
    }
}






