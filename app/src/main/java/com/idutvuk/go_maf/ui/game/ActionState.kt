package com.idutvuk.go_maf.ui.game

import com.idutvuk.go_maf.R


//TODO: add possibility to make any action in any time via holding the main button
enum class ActionState(
    val text: String,
    val icon: Int = R.drawable.ic_debug,
    val requireNumber: Boolean
) {
    //Misc/debug states
    /**
     * Default state
     */
    NEXT(
        text = "Skip",
        icon = R.drawable.ic_arrow_right,
        requireNumber = false
    ),

    /**
     * Debug state. Should not be used in the game
     */
    DEBUG(
        text = "Debug mainBtn state",
        icon = R.drawable.ic_debug,
        requireNumber = false
    ),


    //Start/end game states

    /**
     * Used in the beginning of the game
     */
    START_GAME(
        text = "Start game",
        icon = R.drawable.ic_play,
        requireNumber = false
    ),

    /**
     * End game
     */
    END_GAME(
        text = "End game",
        icon = R.drawable.ic_stop,
        requireNumber = false
    ),

    //DayTimes

    /**
     * Start vote after day speeches
     */
    START_VOTE(
        text = "Start vote",
        requireNumber = false
    ),

    /**
     * Start day after sheriff check
     */
    START_DAY(
        text = "Start day",
        icon = R.drawable.ic_sun,
        requireNumber = false
    ),

    /**
     * Start night after end of the day
     */
    START_NIGHT(
        text = "Start night",
        icon = R.drawable.ic_moon,
        requireNumber = false
    ),

    //Day actions

    /**
     * Starts all day player speeches
     */
    START_SPEECH(
        text = "Start speech",
        requireNumber = false
    ),

    /**
     * Same as "next"
     * TODO: am I really need this?
     */
    @Deprecated("Should not be used in game")
    END_SPEECH(
        text = "End speech",
        requireNumber = false
    ),

    /**
     * Add somebody to the vote while someone is speaking
     */
    ADD_TO_VOTE(
        text = "Add to vote",
        requireNumber = true
    ),

    //vote actions
    /**
     * Vote for kill someone nominated to the elections
     */
    @Deprecated("Currently under development")
    VOTE_FOR(
        text = "THIS NOT SHOULD BE READABLE",
        requireNumber = true
    ),

    /**
     * Temporary state for killing exactly one person on the elections
     */
    KILL_IN_VOTE( //TODO: replace with @VOTE_FOR
        text = "Kill by vote",
        requireNumber = true
    ),

    /**
     * Rerun the elections lap after auto-catastrophe
     */
    RE_VOTE(
        text = "Re-vote",
        requireNumber = true
    //TODO maybe it's more than just require number
    ),

    /**
     * Rerun the elections to the final time
     */
    FINAL_VOTE( //TODO: change the @text
        text = "Final vote",
        requireNumber = true
        //TODO maybe it's more than just require number
    ),

    // Night actions

    /**
     * Mafia speech (sgovorka)
     */
    START_MAFIA_SPEECH(
        text = "Mafia speech",
        requireNumber = false
    ),

    /**
     * Kill a player by mafia after start of the night
     * TODO: add hint: to not kill press 'skip'
     */
    KILL(
        text = "Kill",
        icon = R.drawable.ic_gun_target,
        requireNumber = true
    ),

    /**
     * Check by don after mafia kill at night
     */
    CHECK_DON(
        text = "Don check",
        requireNumber = true
    ),

    /**
     * Check by sheriff after don check at night
     */
    CHECK_SHR(
        text = "Sheriff check",
        requireNumber = true
    ),

    /**
     * Best move
     */
    BEST_MOVE(
        text = "Best move",
        requireNumber = true
    );
    constructor(nominee: String) : this(
        text = "Vote for $nominee",
        requireNumber = true
    )
}