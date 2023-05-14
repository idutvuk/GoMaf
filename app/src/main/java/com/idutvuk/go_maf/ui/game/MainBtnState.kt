package com.idutvuk.go_maf.ui.game

import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.gamedata.PlayerSelectionMode

enum class MainBtnState(
    /**
     * Text that will be shown on the main button
     */
    val text: String,

    /**
     * Icon that will be shown on the main button
     */
    val icon: Int = R.drawable.ic_debug,

    /**
     * Does the action require one or many player numbers to input
     */
    val requireNumber: PlayerSelectionMode,

//    /**
//     * Does the action require additional information
//     * If true -
//     */
//    val requireAdditionalInformation: Boolean = false,
//
    /**
     * Desccription that should describe game state change inside recycler
     */
    val description: String = text
) {
    /**
     * Crashes the app lol
     */
    CRASH(
        text = "It should crash the app",
        requireNumber = PlayerSelectionMode.NONE,
        description = "CRASH BUTTON STATE"
    ),
    //Misc/debug states
    /**
     * Default state
     */
    NEXT(
        text = "Skip",
        icon = R.drawable.ic_arrow_right,
        requireNumber = PlayerSelectionMode.NONE,
        description = "Skipped"
    ),

    /**
     * Debug state. Should not be used in the game
     */
    DEBUG(
        text = "Debug mainBtn state",
        icon = R.drawable.ic_debug,
        requireNumber = PlayerSelectionMode.NONE,
        description = "Debug state passed"
    ),


    //Start/end game states

    /**
     * Used in the beginning of the game
     */
    START_GAME(
        text = "Start game",
        icon = R.drawable.ic_play,
        requireNumber = PlayerSelectionMode.NONE,
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

    /**
     * Temporary state for killing exactly one person on the elections
     */
    KILL_IN_VOTE(
        text = "Kill by vote",
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
     * TODO: add hint: to not mafiaKill press 'skip'
     */
    MAFIA_KILL(
        text = "Kill",
        icon = R.drawable.ic_gun_target,
        requireNumber = PlayerSelectionMode.SINGLE
    ),

    /**
     * Check by don after mafia mafiaKill at night
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
    )
}