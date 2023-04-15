package com.idutvuk.go_maf.model.gameactions

// Define a base interface for all game actions.
interface GameAction {
    fun execute(): Int
    fun undo()
    override fun toString(): String

    /**
     * ## Importance gradation
     * - 0 - Start/End of the game
     * - 1 - Speech/Vote/Night cycle - major interface changes
     * - 2 - Kills & bans - permanent actions
     * - 3 - Night checks
     * - 4 - Speeches
     * - 5 - Fouls & nominatings
     * - 6 - Misc tasks
     */
    abstract val importance: Int
}