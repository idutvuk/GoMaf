package com.idutvuk.go_maf.model.gameactions.speech

import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.gameactions.GameAction

abstract class SpeechAction (val id: Int) : GameAction {
    override fun execute(): Int {
        TODO("Not yet implemented")
    }
    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "speech ${Game.players[id].strNum}"
    }

    override val importance = 4
}