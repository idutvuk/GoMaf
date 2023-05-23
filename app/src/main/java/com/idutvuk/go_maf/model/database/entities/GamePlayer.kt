package com.idutvuk.go_maf.model.database.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_player")
data class GamePlayer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "game_id")
    val gameId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "role_id")
    val roleId: Int,

    @ColumnInfo(name = "experience")
    val experience: Int
)