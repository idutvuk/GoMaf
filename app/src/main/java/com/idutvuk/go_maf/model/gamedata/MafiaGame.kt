package com.idutvuk.go_maf.model.gamedata

import java.sql.Date
import java.sql.Time

data class MafiaGame(
    val date: Date,
    val time: Time,
    val isCompleted: Boolean,
    val players: List<MafiaAccount>,
    val host: MafiaAccount,
    val duration: Time,
    val history: ArrayList<MafiaGameState>
)
