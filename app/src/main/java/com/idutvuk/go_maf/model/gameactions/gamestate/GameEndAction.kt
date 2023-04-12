package com.idutvuk.go_maf.model.gameactions.gamestate

import android.util.Log

class GameEndAction : ChangeGameStateAction() {
    override fun execute(): Int {
        Log.i("GameLog","Game over")
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "game ended"
    }

    override val importance = 0
}