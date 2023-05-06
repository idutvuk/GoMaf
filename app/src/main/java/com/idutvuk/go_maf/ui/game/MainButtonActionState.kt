package com.idutvuk.go_maf.ui.game

import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode


//TODO: add possibility to make any action in any time via holding the main button
enum class MainButtonActionState(
    val text: String,
    val icon: Int = R.drawable.ic_debug,
    val requireNumber: PlayerSelectionMode //TODO: check for necessity
) {
    /**
     * Crashes the app lol
     */
    CRASH(
        text = "It should crash the app",
        requireNumber = PlayerSelectionMode.NONE
    ),
    //Misc/debug states
    /**
     * Default state
     */
    NEXT(
        text = "Skip",
        icon = R.drawable.ic_arrow_right,
        requireNumber = PlayerSelectionMode.NONE
    ),

    /**
     * Debug state. Should not be used in the game
     */
    DEBUG(
        text = "Debug mainBtn state",
        icon = R.drawable.ic_debug,
        requireNumber = PlayerSelectionMode.NONE
    ),


    //Start/end game states

    /**
     * Used in the beginning of the game
     */
    START_GAME(
        text = "Start game",
        icon = R.drawable.ic_play,
        requireNumber = PlayerSelectionMode.NONE
    ),

    /**
     * End game
     */
    END_GAME(
        text = "End game",
        icon = R.drawable.ic_stop,
        requireNumber = PlayerSelectionMode.NONE
    ),

    //DayTimes

    /**
     * Start vote after day speeches
     */
    START_VOTE(
        text = "Start vote",
        requireNumber = PlayerSelectionMode.MULTIPLE
    ),

    /**
     * Start day after sheriff check
     */
    START_DAY(
        text = "Start day",
        icon = R.drawable.ic_sun,
        requireNumber = PlayerSelectionMode.NONE
    ),

    /**
     * Start night after end of the day
     */
    START_NIGHT(
        text = "Start night",
        icon = R.drawable.ic_moon,
        requireNumber = PlayerSelectionMode.NONE
    ),

    //Day actions

    /**
     * Starts all day player speeches
     */
    START_SPEECH(
        text = "Start speech",
        requireNumber = PlayerSelectionMode.NONE
    ),

    /**
     * Same as "next"
     * TODO: am I really need this?
     */
    @Deprecated("Should not be used in game")
    END_SPEECH(
        text = "End speech",
        requireNumber = PlayerSelectionMode.NONE
    ),

    /**
     * Add somebody to the vote while someone is speaking
     */
    ADD_TO_VOTE(
        text = "Add to vote",
        requireNumber = PlayerSelectionMode.SINGLE
    ),

    //vote actions
    /**
     * Vote for killByMafia someone nominated to the elections
     */
    @Deprecated("Currently under development")
    VOTE_FOR(
        text = "THIS NOT SHOULD BE READABLE",
        requireNumber = PlayerSelectionMode.MULTIPLE
    ),

    /**
     * Temporary state for killing exactly one person on the elections
     */
    KILL_IN_VOTE( //TODO: replace with @VOTE_FOR
        text = "Kill by vote",
        requireNumber = PlayerSelectionMode.SINGLE
    ),

    /**
     * Rerun the elections lap after auto-catastrophe
     */
    AUTOCATASTROPHE(
        text = "Re-vote",
        requireNumber = PlayerSelectionMode.MULTIPLE
    ),

    /**
     * Rerun the elections to the final time
     */
    FINAL_VOTE( //TODO: change the @text
        text = "Final vote",
        requireNumber = PlayerSelectionMode.MULTIPLE
    ),

    // Night actions

    /**
     * Mafia speech (sgovorka)
     */
    START_MAFIA_SPEECH(
        text = "Mafia speech",
        requireNumber = PlayerSelectionMode.NONE
    ),

    /**
     * Kill a player by mafia after start of the night
     * TODO: add hint: to not killByMafia press 'skip'
     */
    KILL(
        text = "Kill",
        icon = R.drawable.ic_gun_target,
        requireNumber = PlayerSelectionMode.SINGLE
    ),

    /**
     * Check by don after mafia killByMafia at night
     */
    CHECK_DON(
        text = "Don check",
        requireNumber = PlayerSelectionMode.SINGLE
    ),

    /**
     * Check by sheriff after don check at night
     */
    CHECK_SHR(
        text = "Sheriff check",
        requireNumber = PlayerSelectionMode.SINGLE
    ),

    /**
     * Best move
     */
    BEST_MOVE(
        text = "Best move",
        requireNumber = PlayerSelectionMode.MULTIPLE
    ),

    /**
     * Similar to NEXT, used when need to select the button
     * Currently only for single-click
     * //TODO: expand or declare as single-click
     */
    WAITING_FOR_CLICK(
        text = "Cancel",
        requireNumber = PlayerSelectionMode.NONE
    );

    constructor(nominee: String) : this(
        text = "Vote for $nominee",
        requireNumber = PlayerSelectionMode.SINGLE
    )
}