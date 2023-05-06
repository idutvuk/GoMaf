package com.idutvuk.go_maf.model.gamedata

import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.ui.game.MainButtonActionState
import java.lang.Error

/**
 * The state of a game.
 * It should contain all the information about game right now.
 */
class MafiaGameState(
    var numPlayers: Int = 10,
    var players: Array<Player> = Array(numPlayers) { Player(it) },
    var voteList: MutableSet<Player> = mutableSetOf(),
    var leaderVoteList: MutableSet<Player> = mutableSetOf(),
    var gameOver: Boolean = false,

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


    var mainButtonActionState: MainButtonActionState = MainButtonActionState.START_GAME,
    var previousMainButtonActionState: MainButtonActionState = MainButtonActionState.CRASH,
    var delayedMainButtonActionState: MainButtonActionState = MainButtonActionState.CRASH,

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

    fun livingPlayersCount(): Int {
        var livingPlayers = 0
        for (i in 0 until Game.numPlayers) {
            if (players[i].alive) {
                livingPlayers++
            }
        }
        return livingPlayers
    }

    /**
     * Returns closest alive player to selected player
     * _Example:_
     * 0-1-2-3-4-5-[6]-7-8-9
     * Returns 6 if 6 is alive
     * Returns 7 if 6 is dead and 7 is alive
     */
    fun closestAlivePlayer(cursor: Int): Int {
        if (players[cursor].alive) return cursor
        return nextAlivePlayer(cursor)
    }

    /**
     * returns next alive player
     */
    fun nextAlivePlayer(cursor: Int): Int {
        //check for alive players:
        val livingPlayers = livingPlayersCount()
        assert(livingPlayers>1)
        //if reached the end
        if (cursor >= Game.numPlayers - 1) return nextAlivePlayer(-1)

        //if next player is alive
        if (players[cursor + 1].alive) return cursor + 1

        //if next player is dead
        return nextAlivePlayer(cursor + 1)
    }


    //TODO: implement double, triple and more kill
    fun kill(index: Int){
        players[index].alive = false
        gameOver = false
        return //TODO: remove (early return only for the debug purposes!)
        var redCounter = 0
        var blackCounter = 0
        for (i in 0 until Game.numPlayers) {
            if (players[i].role.isRed) redCounter++ else blackCounter++
        }
        if (redCounter <= blackCounter) gameOver = true //black wins
        if (blackCounter <= 0) gameOver = true //red wins
        gameOver = false //game continues
    }

    override fun toString(): String {
        return "MafiaGameState(numPlayers=$numPlayers, players=${players.contentToString()}, voteList=$voteList, leaderVoteList=$leaderVoteList, gameOver=$gameOver, currentPhaseNumber=$currentPhaseNumber, time=$time, mainButtonActionState=$mainButtonActionState, previousMainButtonActionState=$previousMainButtonActionState, delayedMainButtonActionState=$delayedMainButtonActionState, mainButtonOverwriteString='$mainButtonOverwriteString', firstSpokedPlayer=$firstSpokedPlayer, cursor=$cursor, selectedPlayers=$selectedPlayers, selectionMode=$selectionMode, selectionRequested=$selectionRequested, headingText='$headingText', descriptionText='$descriptionText', mafiaMissStreak=$mafiaMissStreak, isMafiaMissedToday=$isMafiaMissedToday, isTimerActive=$isTimerActive)"
    }

//    override fun toString(): String {
//        return "MafiaGameState(\n" +
//                "numPlayers=$numPlayers, \n" +
//                "players=${players.contentToString()}, \n" +
//                "voteList=$voteList, \n" +
//                "isOver=$gameOver, \n" +
//                "time=$time, \n" +
//                "is timer active = $isTimerActive, \n" +
//                "mainButtonActionState=$mainButtonActionState, \n" +
//                "previous mainButtonActionState=$delayedMainButtonActionState, \n" +
//                "delayed mainButtonActionState=$delayedMainButtonActionState, \n" +
//                "selected=$selectedPlayers, \n" +
//                "headingText='$headingText, \n" +
//                "descriptionText='$descriptionText\n" +
//                ")"
//    }
}


