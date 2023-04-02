package com.idutvuk.go_maf.model.gameactions

// Define a base interface for all game actions.
interface GameAction {
    fun execute(): Int
    fun undo()
}