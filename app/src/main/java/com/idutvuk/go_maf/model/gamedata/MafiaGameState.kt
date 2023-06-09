package com.idutvuk.go_maf.model.gamedata

import android.util.Log
import com.idutvuk.go_maf.model.gamedata.MainBtnState.*
import com.idutvuk.go_maf.model.generateRoles
import java.lang.IllegalStateException

/**
 * The state of a game.
 * It should contain all the information about game right now.
 */
data class MafiaGameState(
    val numPlayers: Int,
    val roles: Array<Role> = generateRoles(numPlayers),
    var players: Array<Player> = Array(numPlayers) {
        Player(
            number = it,
            role = roles[it]
        )
                                                   },
    var livingPlayers: List<Boolean> = players.map { it.alive },
    /**
     * Queue of players wanting to speak
     * used in vote dead speeches
     * ...or vote before-dead speeches
     */
    var speakQueue: ArrayList<Int>? = null,
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


    var mainBtnState: MainBtnState = START_GAME,
    var prevMainBtnState: MainBtnState = CRASH,
    var delayedBtnState: MainBtnState = CRASH,

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
     * NONE - you can't select anyone
     * SINGLE - you can select someone, but only one person
     * MULTIPLE - you can select anyone you want
     */
    var selectionMode: PlayerSelectionMode = PlayerSelectionMode.NONE,

    /**
     * TODO: If selection requested, players should start shaking
     */
    var selectionRequested: Boolean = false,

    var snackbarMessage: String? = null,

    /**
     * changes heading on the top of the fragment
     */
    var primaryMessage: String = "Default heading",

    /**
     * changes description on the top of the fragment
     */
    var secondaryMessage: String = "Default description",

    /**
     * if mafia missed three times, game ends
     * used for best move calculation
     */
    var mafiaMissStreak: Int = 0,

    /**
     * true only at night if mafia mafiaKill was cancelled
     * used in skip night
     */
    private var isMafiaMissedToday: Boolean = false,
    /**
     * toggles the timer (via LiveData)
     */
    var isTimerActive: Boolean = false,

    val voteList:ArrayList<Int> = arrayListOf(),
    var isVoteCancelled: Boolean = false,
    var selectedPlayers: ArrayList<Int> = arrayListOf(),

    var canUndo: Boolean = false,

    var snapshotHistory: ArrayList<StateSnapshot> = arrayListOf(),
) {

    fun addToVoteList(index: Int): Boolean {
        return voteList.add(index)
    }

    fun addToVoteList(): Boolean {
        assert(selectedPlayers.size == 1)
        return addToVoteList(selectedPlayers[0])
    }

    fun clearVoteList() {
        voteList.clear()
    }

    @Deprecated("use compose")
    fun togglePlayerSelection(index: Int) {
        if (!selectedPlayers.contains(index)) {
            selectedPlayers.add(index)
        } else {
            selectedPlayers.remove(index)
        }
    }

    @Deprecated("use compose")
    fun clearSelection() {
        selectedPlayers.clear()
    }

    fun livingPlayersCount(): Int {
        var livingPlayers = 0
        for (i in 0 until numPlayers) {
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
        assert(livingPlayers > 1)
        //if reached the end
        if (cursor >= numPlayers - 1) return nextAlivePlayer(-1)

        //if next player is alive
        if (players[cursor + 1].alive) return cursor + 1

        //if next player is dead
        return nextAlivePlayer(cursor + 1)
    }

    fun failedMafiaKill() {
        isMafiaMissedToday = true
        isGameOver()
    }

    fun mafiaKill() {
        isMafiaMissedToday = false
        mafiaMissStreak = 0
        kill(selectedPlayers[0])
    }


    private fun kill(i: Int) {
        players[i].alive = false
        snackbarMessage = "killed $i"
        livingPlayers = players.map { it.alive }
        gameOver = isGameOver()
    }


    fun voteKill(index: Int) {
        kill(index)
    }

    /**
     * returns false if...
     * ...there is no alive black players
     * ...amount of alive black players is not less than amount of red players
     * ...if mafia missed three times
     */
    fun isGameOver(): Boolean {
        if (mafiaMissStreak >= 3) return true
        var redCounter = 0
        var blackCounter = 0
        for (i in 0 until numPlayers) {
            if (players[i].alive)
                if (players[i].role.isRed) redCounter++ else blackCounter++
        }
        if (redCounter <= blackCounter) return true //black wins
        if (blackCounter <= 0) return true //red wins
        return false
    }

    fun checkDon(): Boolean {
        assert(selectedPlayers.size == 1)
        return players[selectedPlayers[0]].role == Role.SHR
    }

    fun checkShr(): Boolean {
        assert(selectedPlayers.size == 1)
        return players[selectedPlayers[0]].role.isRed
    }

    fun skipNight() {
        if (!isMafiaMissedToday) failedMafiaKill()
        if (gameOver)
            mainBtnState = END_GAME
        else {
            isTimerActive = false
            currentPhaseNumber++
            secondaryMessage = currentPhaseNumber.toString()
            time = GameTime.DAY
            firstSpokedPlayer = nextAlivePlayer(firstSpokedPlayer)
            cursor = firstSpokedPlayer
            mainBtnState = START_SPEECH
        }
    }

    fun canDoBestMove(): Boolean {
        if (isMafiaMissedToday) return false
        if (currentPhaseNumber != 2) return false
        return livingPlayersCount() + 2 >= numPlayers
    }

    fun allPlayersSpoked(): Boolean {
        assert(time == GameTime.DAY)
        return closestAlivePlayer(cursor) != closestAlivePlayer(firstSpokedPlayer)
    }


    fun nextMainBtnState() {
        if (!gameOver) mainBtnState =
            when (mainBtnState) {
                NEXT -> delayedBtnState

                DEBUG -> {
                    Log.d("GameLog", "MGS: debug pressed")
                    START_GAME
                }

                START_GAME -> START_NIGHT

                END_GAME -> END_GAME //todo something on the game end

                START_VOTE -> {
                    when (voteList.size) {
                        0 -> START_NIGHT
                        1 -> if (currentPhaseNumber == 2) START_NIGHT else START_SPEECH
                        else -> KILL_IN_VOTE
                    }
                }

                START_DAY -> START_SPEECH

                START_NIGHT -> if (currentPhaseNumber <= 1) START_MAFIA_SPEECH else MAFIA_KILL

                START_SPEECH -> {
                    if (speakQueue != null) {
                        if (speakQueue!!.isNotEmpty()) END_SPEECH
                        else CRASH
                    } else {
                        delayedBtnState = END_SPEECH
                        ADD_TO_VOTE
                    }
                }

                END_SPEECH -> if (speakQueue == null) {
                    if (allPlayersSpoked()) START_SPEECH else if (!isVoteCancelled) START_VOTE else START_NIGHT
                } else if (speakQueue!!.isEmpty()) START_NIGHT else START_SPEECH

                ADD_TO_VOTE -> nextStateSingleClick(END_SPEECH)


                KILL_IN_VOTE -> if (speakQueue == null) START_NIGHT else START_SPEECH


                START_MAFIA_SPEECH -> {
                    delayedBtnState = CHECK_DON
                    NEXT
                }

                MAFIA_KILL -> nextStateSingleClick(END_GAME)

                CHECK_DON -> nextStateSingleClick(CHECK_SHR)

                CHECK_SHR -> nextStateSingleClick(if (canDoBestMove()) BEST_MOVE else START_DAY)

                BEST_MOVE -> START_DAY //todo waiting for click

                WAITING_FOR_CLICK -> delayedBtnState

                else -> CRASH
            } else END_GAME
    }


    /**
     * возвращает следующую фазу, если выделение игрока уже произошло.
     * В противном случае ставит необходимую фазу в delay и возвращает ожидание клика
     */
    private fun nextStateSingleClick(nextPhase: MainBtnState): MainBtnState {
        assert(mainBtnState.requireNumber == PlayerSelectionMode.SINGLE)
        if (selectedPlayers.size == 1) return nextPhase
        delayedBtnState = nextPhase
        prevMainBtnState = mainBtnState
        return WAITING_FOR_CLICK
    }

    fun foul(i: Int) {
        when (players[i].fouls) {
            0, 1, 2 -> players[i].fouls++
            2 -> {
                players[i].mute()
            }

            3 -> {
                kill(i)
                isVoteCancelled = true
            }

            else -> throw IllegalStateException("Incorrect value of fouls")
        }
    }

    override fun toString(): String {
        return "MafiaGameState(players=${players.contentToString()}, speakQueue=$speakQueue, gameOver=$gameOver, time=$time, cursor=$cursor, selectionMode=$selectionMode, primaryMessage='$primaryMessage', secondaryMessage='$secondaryMessage', mafiaMissStreak=$mafiaMissStreak, isVoteCancelled=$isVoteCancelled, selectedPlayers=$selectedPlayers), importance=${mainBtnState.importance}"
    }


}



