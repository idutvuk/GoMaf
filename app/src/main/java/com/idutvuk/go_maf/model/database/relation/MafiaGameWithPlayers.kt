package com.idutvuk.go_maf.model.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.entities.MafiaGamePlayer

data class MafiaGameWithPlayers (
    @Embedded val mafiaGame: MafiaGame,
    @Relation(
        parentColumn = "id",
        entityColumn = "gameId",
        entity = MafiaGamePlayer::class,
        projection = ["userId"]
    ) val userIdList: List<Long>,
)