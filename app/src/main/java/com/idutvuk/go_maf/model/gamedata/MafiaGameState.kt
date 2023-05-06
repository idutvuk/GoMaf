package com.idutvuk.go_maf.model.gamedata

import com.idutvuk.go_maf.ui.game.MainButtonActionState

/**
 * The state of a game.
 * It should contain all the information about game right now.
 */
class MafiaGameState(
    var numPlayers: Int = 10,
    var players: Array<Player> = Array(numPlayers) { Player(it) },
    var voteList: MutableSet<Player> = mutableSetOf(),
    var leaderVoteList: MutableSet<Player> = mutableSetOf(),
    var isOver: Boolean = false,

    /**
     * ## passed nights + passed days + current phase.
     * - pP%2==0 -> currently is day
     * - pP%2==1 -> currently is night
     * - pP//2 -> passed cycles
     */
    var currentPhaseNumber: Int = 0,

    /**
     * current game time
     *
     * Can be only DAY & NIGHT
     */
    var time: GameTime = GameTime.NIGHT,


    var mainButtonActionState: MainButtonActionState = MainButtonActionState.DEBUG,
    var previousMainButtonActionState: MainButtonActionState = MainButtonActionState.DEBUG,
    var delayedMainButtonActionState: MainButtonActionState = MainButtonActionState.DEBUG,

    /**
     * if not empty, overwrites the main button text
     */
    var mainButtonOverwriteString: String = "",

    /**
     * used for prevent infinite-speech loop
     */
    var firstSpokedPlayer: Int = -1,

    /**
     * Cursor - indication - which player is speaking rn
     *
     * _cursor is unchangeable from the user_
     */
    var cursor: Int = 0,

    /**
     * Selected players is a selector of the voted players
     * like cursor, but used when multiple players selected
     */
    var selectedPlayers: ArrayList<Int> = arrayListOf(),

    /**
     * NONE - you can't select anyone
     * SINGLE - you can select someone, but only one person
     * MULTIPLE - you can select anyone you want
     */
    var selectionMode: PlayerSelectionMode = PlayerSelectionMode.NONE,

    /**
     * TODO: If selection requested, players should start shaking
     */
    var selectionRequested: Boolean = false,

    /**
     * changes heading on the top of the fragment
     */
    var headingText: String = "Default heading",

    /**
     * changes description on the top of the fragment
     */
    var descriptionText: String = "Default description",

    /**
     * if mafia missed three times, game ends
     * used for best move calculation
     */
    var mafiaMissStreak: Int = 0,

    /**
     * true only at night if mafia kill was cancelled
     * used in skip night
     */
    var isMafiaMissedToday: Boolean = false,
    /**
     * toggles the timer (via LiveData)
     */
    var isTimerActive: Boolean = false,
    ) {
    override fun toString(): String {
        return "MafiaGameState(\n" +
                "numPlayers=$numPlayers, \n" +
                "players=${players.contentToString()}, \n" +
                "voteList=$voteList, \n" +
                "isOver=$isOver, \n" +
                "time=$time, \n" +
                "is timer active = $isTimerActive, \n" +
                "mainButtonActionState=$mainButtonActionState, \n" +
                "previous mainButtonActionState=$delayedMainButtonActionState, \n" +
                "delayed mainButtonActionState=$delayedMainButtonActionState, \n" +
                "selected=$selectedPlayers, \n" +
                "headingText='$headingText, \n" +
                "descriptionText='$descriptionText\n" +
                ")"
    }
}


