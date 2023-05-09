package com.idutvuk.go_maf.model.gamedata

import android.util.Log
import com.idutvuk.go_maf.ui.game.MainBtnState
import com.idutvuk.go_maf.ui.game.MainBtnState.*

/**
 * The state of a game.
 * It should contain all the information about game right now.
 */
class MafiaGameState(
    val numPlayers: Int,
    var players: Array<Player> = Array(numPlayers) { Player(it) },
//    var leaderVoteList: MutableSet<Int> = mutableSetOf(),
    /**
     * Queue of players wanting to speak
     * used in vote dead speeches
     * ...or vote before-dead speeches
     */
    var speakQueue: MutableSet<Int>? = null,
    private var gameOver: Boolean = false,

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
    var previousMainButtonActionState: MainBtnState = CRASH,
    var delayedBtnState: MainBtnState = CRASH,

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
) {

    private var voteList: MutableSet<Int> = mutableSetOf()

    val voteListCopy: Set<Int>
        get() = voteList.toSet()

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

    /**
     * Selected players is a selector of the voted players
     * like cursor, but used when multiple players selected
     */
    private val selectedPlayers: ArrayList<Int> = arrayListOf()

    val selectedPlayersCopy: Set<Int>
        get() = selectedPlayers.toSet()

    fun togglePlayerSelection(index: Int) {
        if (!selectedPlayers.contains(index)) {
            selectedPlayers.add(index)
        } else {
            selectedPlayers.remove(index)
        }
    }

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
        return nextAlivePlayer(cursor) != closestAlivePlayer(firstSpokedPlayer)
    }

    /**
     *
     * adds selected players to the vote
     * disables the selected players
     *
     */
//    fun voteFor() {
//        for (i in 0 until voteList.size) {
//            if (i != voteList.size - 1) { //last player remaining
//                for (voter in players) {
//                    if (!voter.alive) continue
//                    if (!voter.voted) players[voteList.last()].votedPlayers!!.add(voter) //add non-voted players
//
//                    //back enabling all the players
//                    voter.isEnabled = true
//                    voter.voted = false
//                                }
//            } else if (players[voteList.elementAt(i)].votedPlayers == null) { //player was not voted
//                players[voteList.elementAt(i)].votedPlayers = mutableSetOf()
//                for (voter in selectedPlayers) {
//                    players[voteListCopy.elementAt(i)].votedPlayers!!.add(players[voter])
//                    players[voter].voted = true
//                    players[voter].isEnabled = false
//                }
//                break
//            }
//        }  //all players voted
//    }

    fun voteForActive() {
        Log.d("GameLog", "cursor: $cursor, vote list: $voteList")
        assert(cursor in voteListCopy)
        players[cursor].votedPlayers = selectedPlayers.toMutableSet()
    }

    /**
     * calculates new leader voteList and can auto-catastrophe happen or not
     */
    fun checkAutoCatastrophe(): Boolean {
        assert(voteList.isNotEmpty())
        val newVoteList = mutableSetOf<Int>()
        var leaderScore = 0
        var canAutoCatastropheHappen = false
        for (i in voteList) {
            val count = players[i].votedPlayers!!.size
            if (count < leaderScore) {

            } else if (count == leaderScore) {
                newVoteList.add(i)
                canAutoCatastropheHappen = true
            } else {
                canAutoCatastropheHappen = false
                leaderScore = count
                newVoteList.clear()
                newVoteList.add(i)
            }
    }
    voteList = newVoteList
    return canAutoCatastropheHappen
}


/**
 * marks selected players as voters
 * for killing all the people
 *
 * if voted for killing all the people...
 * ...adds players to the final speech
 *
 * returns...
 * ...TRUE if all killed
 * ...FALSE if all alive
 */
fun finalVote(): Boolean =
    if (selectedPlayers.size > numPlayers / 2) { //all vote leaders leaving
        speakQueue = selectedPlayers.toMutableSet()
        true
    } else false //all vote leaders remaining

fun nextMainBtnState() {
    if (!gameOver) mainBtnState =
        when (mainBtnState) {
            NEXT -> delayedBtnState

            DEBUG -> {
                Log.d("GameLog", "s")
                START_GAME
            }

            START_GAME -> START_NIGHT

            END_GAME -> END_GAME //todo something on the game end

            START_VOTE -> if (voteListCopy.size == 1) START_SPEECH //last speech
            else VOTE_FOR

            START_DAY -> START_SPEECH

            START_NIGHT -> if (currentPhaseNumber <= 1) START_MAFIA_SPEECH else MAFIA_KILL

            START_SPEECH -> {
                if (speakQueue != null) END_SPEECH
                else {
                    delayedBtnState = END_SPEECH
                    ADD_TO_VOTE
                }
            }

            END_SPEECH -> if (allPlayersSpoked()) START_SPEECH else START_VOTE

            ADD_TO_VOTE -> nextStateSingleClick(END_SPEECH)

            VOTE_FOR -> {
                if (voteList.indexOf(cursor) == voteList.size - 1) { // if last
                    START_SPEECH
                }
                    VOTE_FOR
            }

            KILL_IN_VOTE -> TODO()

            FINAL_VOTE -> {
                if (speakQueue!!.size != 0) {
                    cursor = speakQueue!!.first()
                    START_SPEECH
                } else {
                    START_NIGHT
                }
            }

            START_MAFIA_SPEECH -> {
                delayedBtnState = CHECK_DON
                NEXT
            }

            MAFIA_KILL -> nextStateSingleClick(CHECK_DON)

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
fun nextStateSingleClick(nextPhase: MainBtnState): MainBtnState {
    assert(mainBtnState.requireNumber == PlayerSelectionMode.SINGLE)
    if (selectedPlayersCopy.size == 1) return nextPhase
    delayedBtnState = nextPhase
    previousMainButtonActionState = mainBtnState
    return WAITING_FOR_CLICK
}


override fun toString(): String {
    return "MafiaGameState(numPlayers=$numPlayers, players=${players.contentToString()}, voteList=$voteList, gameOver=$gameOver, currentPhaseNumber=$currentPhaseNumber, time=$time, mainBtnState=$mainBtnState, previousMainButtonActionState=$previousMainButtonActionState, delayedMainButtonActionState=$delayedBtnState, mainButtonOverwriteString='$mainButtonOverwriteString', firstSpokedPlayer=$firstSpokedPlayer, cursor=$cursor, selectedPlayers=$selectedPlayers, selectionMode=$selectionMode, selectionRequested=$selectionRequested, primaryMessage='$primaryMessage', secondaryMessage='$secondaryMessage', mafiaMissStreak=$mafiaMissStreak, isMafiaMissedToday=$isMafiaMissedToday, isTimerActive=$isTimerActive)"
}
}


