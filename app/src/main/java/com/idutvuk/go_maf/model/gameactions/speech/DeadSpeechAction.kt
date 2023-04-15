package com.idutvuk.go_maf.model.gameactions.speech

import com.idutvuk.go_maf.model.Game

class DeadSpeechAction (playerId: Int) : PlayerSpeechAction(playerId) {
    override fun execute(): Int {
        TODO("Not yet implemented")
    }
    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "final speech ${Game.players[id].strNum}"
    }
}