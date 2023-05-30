package com.idutvuk.go_maf.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mafiaGamePlayer")
data class MafiaGamePlayer(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val gameId: Long,
    val userId: Long
)