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
//    val players: List<MafiaAccount>,
//    val host: MafiaAccount,

    @ColumnInfo(name = "duration")
    val duration: Time,
//    val history: ArrayList<MafiaGameState>
)
