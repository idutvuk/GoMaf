package com.idutvuk.go_maf.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

@Entity
data class MafiaGame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="date")
    val date: Date,

    @ColumnInfo(name="time")
    val time: Time,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,

    @ColumnInfo(name = "host")
    val host: MafiaAccount, //todo maybe serialize it to gson?

    @ColumnInfo(name = "players")
    var players: ArrayList<MafiaAccount>, //todo maybe serialize it to gson?

    @ColumnInfo(name = "duration")
    val duration: Time,
//    val history: ArrayList<MafiaGameState>
)
