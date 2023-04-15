package com.idutvuk.go_maf.model

import com.idutvuk.go_maf.model.gameactions.CheckDonAction
import com.idutvuk.go_maf.model.gameactions.CheckShrAction
import com.idutvuk.go_maf.model.gameactions.gamestate.StartDayAction
import com.idutvuk.go_maf.model.gameactions.gamestate.StartNightAction
import com.idutvuk.go_maf.model.gameactions.gamestate.StartVoteAction
import com.idutvuk.go_maf.model.gameactions.kill.KillByMafiaAction
import com.idutvuk.go_maf.model.gameactions.speech.MafiaSpeechAction
import com.idutvuk.go_maf.model.gameactions.speech.RegularPlayerSpeechAction

object GameLogic {
    private var currentSpeaker: Int = 0

    init {
        with(CmdManager) {
            commit(MafiaSpeechAction())
            //woke up don
            //woke up shr
            while (Game.gameActive) {
                commit(StartDayAction())

                while (currentSpeaker < Game.numPlayers) {
                    commit(RegularPlayerSpeechAction(currentSpeaker))
                }
                //-------VOTE PHASE----------
                commit(StartVoteAction())
                //TODO: Voting logic


//
                commit(StartNightAction())

//                commit(KillByMafiaAction())
//                commit(CheckDonAction())
//                commit(CheckShrAction())
            }
        }
    }

}