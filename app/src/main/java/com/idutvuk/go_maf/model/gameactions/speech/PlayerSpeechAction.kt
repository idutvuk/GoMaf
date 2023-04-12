package com.idutvuk.go_maf.model.gameactions.speech

import com.idutvuk.go_maf.model.Game


/**
 * Речь игрока, может длиться 60 или 30 секунд.
 */
abstract class PlayerSpeechAction(mId: Int) : SpeechAction(mId) {
    override fun execute(): Int {
        TODO("Not yet implemented")
    }
    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "speech Player ${Game.players[id].strNum}"
    }
}