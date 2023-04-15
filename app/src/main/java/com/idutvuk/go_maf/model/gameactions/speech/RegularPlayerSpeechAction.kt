package com.idutvuk.go_maf.model.gameactions.speech

import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.gameactions.GameAction

class RegularPlayerSpeechAction (mId: Int) : PlayerSpeechAction(mId) {
    override fun execute(): Int {
        //TODO something here...
        return 0
    }
    override fun undo() {
        Game.currentSpeaker--
    }

    override fun toString(): String {
        return "speech ${Game.players[id].strNum}"
    }
}