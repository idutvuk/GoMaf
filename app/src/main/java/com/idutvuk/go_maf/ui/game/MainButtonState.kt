package com.idutvuk.go_maf.ui.game

import com.idutvuk.go_maf.R

enum class MainButtonState(val text: String, val icon: Int) {

    /**
     * Default state
     */
    NEXT("Skip",R.drawable.ic_arrow_right),

    /**
     * Debug state. Should not be used
     */
    DEBUG("Sdobalver",R.drawable.ic_debug),

    /**
     * Used in the beginning of the game
     */
    START_GAME("Start", R.drawable.ic_play),

    /**
     * Start vote after day speeches
     */
    START_VOTE("To vote",R.drawable.ic_debug),

    /**
     * Start day after sheriff check
     */
    START_DAY("Start day", R.drawable.ic_sun),

    /**
     * Start night after end of the day
     */
    START_NIGHT("Start night",R.drawable.ic_moon),

    /**
     * Kill a player by mafia after start of the night
     * TODO: add hint: to not kill press 'skip'
     */
    KILL("Kill",R.drawable.ic_gun_target),

    /**
     * Check by don after mafia kill at night
     */
    CHECK_DON("Don check",R.drawable.ic_debug),

    /**
     * Check by sheriff after don check at night
     */
    CHECK_SHR("Sheriff check",R.drawable.ic_debug)

    //TODO: Bug: icon not changing sometimes
    //TODO: UI BUG: shadow cropping
}