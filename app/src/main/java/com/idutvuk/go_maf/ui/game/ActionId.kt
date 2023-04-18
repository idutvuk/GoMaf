package com.idutvuk.go_maf.ui.game

/**
 * Temporary class for selecting action mode
 */
enum class ActionId {

    /**
     * Kill someone by mafia
     */
    KILL,

    /**
     * Add to the vote
     */
    VOTE,

    /**
     * Check by sheriff
     */
    CSHR,

    /**
     * Check by don
     */
    CDON,

    /**
     * Add foul
     */
    FOUL,

    /**
     * When nothing is selected
     */
    NONE
}
