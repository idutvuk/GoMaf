package com.idutvuk.go_maf.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idutvuk.go_maf.model.database.MafiaAccount.Companion.users
import java.sql.Date
import java.sql.Time

@Entity(tableName = "games")
data class MafiaGame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="date")
    val date: Date,

    @ColumnInfo(name="time")
    val time: Time,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,

//    @ColumnInfo(name = "host")
//    val host: MafiaAccount, //todo maybe serialize it to gson?

    @ColumnInfo(name = "num_players")
    val numPlayers: Int,

//    @ColumnInfo(name = "players") //todo uncomment
//    var players: ArrayList<MafiaAccount>, //todo maybe serialize it to gson?

    @ColumnInfo(name = "duration")
    val duration: Time,
//    val history: ArrayList<MafiaGameState>
) {
    companion object {
        val gameDates = (16409L..16638L).shuffled().take(20).map { it*100000L } // UNIX timestamps between 2022-01-01 and 2023-12-31
        val gameTimes = (0L..43200L).shuffled().take(20).map { Time(it * 1000) } // random times between 12:00 AM and 12:00 PM
        val numPlayers = (6..12).toList()
        val games by lazy {
            List(20) {
                val startTime = Time((gameDates[it] * 1000) + gameTimes[it].seconds)
                val duration = Time((30L..75L).random() * 60 * 1000)
                val players = users.shuffled().take(numPlayers.random()).toCollection(arrayListOf())
                MafiaGame(
                    date = Date(gameDates[it] * 1000),
                    time = startTime,
                    duration = duration,
                    isCompleted = false,
//                    host = users[0],
                    numPlayers = players.size,
//                    players = players
                )
            }
        }
    }
}
