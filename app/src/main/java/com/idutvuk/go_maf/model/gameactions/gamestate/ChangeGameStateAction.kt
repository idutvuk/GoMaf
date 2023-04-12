package com.idutvuk.go_maf.model.gameactions.gamestate

import com.idutvuk.go_maf.model.gameactions.GameAction

abstract class ChangeGameStateAction : GameAction {
    override val importance = 1
}