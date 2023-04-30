package com.idutvuk.go_maf.model.gamedata

import com.idutvuk.go_maf.ui.game.ActionState

/**
 * The state of a game.
 * It should contain all the information about game right now.
 */
class MafiaGameState(
    var numPlayers: Int = 10,
    var players: Array<Player> = Array(numPlayers) { Player(it) },
    var voteList: ArrayList<Player> = ArrayList(),
    var isOver: Boolean = false,

    /**
     * ## passed nights + passed days.
     * - pP%2==0 -> night
     * - pP%2==1 -> day
     * - pP//2 -> passed cycles
     */
    var passedPhases: Int = 0,
    var time: GameTime = GameTime.NIGHT,
    var actionState: ActionState = ActionState.DEBUG,
    var mainButtonOverwriteString: String = "",
    /**
     * Selected players is a selector of currently speaking player
     */
    var cursor: Int = 0,

    /**
     * Selected players is a selector of the voted players
     * like cursor, but used when multiple players selected
     */
    var selectedPlayers: ArrayList<Int> = arrayListOf(),
    var headingText: String = "Default heading",
    var descriptionText: String = "Default description",
    ) {
    override fun toString(): String {
        return "MafiaGameState(" + '\n' +
                "numPlayers=$numPlayers, " + '\n' +
                "players=${players.contentToString()}, " + '\n' +
                "voteList=$voteList, " + '\n' +
                "isOver=$isOver, " + '\n' +
                "time=$time, " + '\n' +
                "actionState=$actionState, " + '\n' +
                "selected=$selectedPlayers, " + '\n' +
                "headingText='$headingText', " + '\n' +
                "descriptionText='$descriptionText'" +
                ")"
    }
}

