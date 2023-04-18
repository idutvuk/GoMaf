package com.idutvuk.go_maf.model.gamedata

class GameState(
    val numPlayers: Int = 10,
    val players: Array<Player> = Array(numPlayers) { Player(it) },
    var voteList: ArrayList<Int> = ArrayList(),
    val isOver: Boolean = false,
    val phase: GamePhase = GamePhase.START,
    val time: GameTime = GameTime.NIGHT
    )


