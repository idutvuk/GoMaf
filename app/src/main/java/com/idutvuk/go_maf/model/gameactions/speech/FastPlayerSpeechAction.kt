package com.idutvuk.go_maf.model.gameactions.speech

import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.gameactions.GameAction

class FastPlayerSpeechAction (playerId: Int) : PlayerSpeechAction(playerId) {
    override fun execute(): Int {
        TODO("Not yet implemented")
    }
    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "speech ${Game.players[id].strNum}"
    }
}