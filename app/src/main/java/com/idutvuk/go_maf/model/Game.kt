package com.idutvuk.go_maf.model

import android.util.Log
import com.google.android.material.button.MaterialButton
import com.idutvuk.go_maf.ui.game.SmartTV

object Game {
    const val minPlayers = 6
    const val maxPlayers = 12
    var numPlayers = -1
        set(value) {
            if (numPlayers==-1) {
                field = value
                Log.i("GameLog", "Players number is set as $value")
            } else {
                Log.e("GameLog","Attempt to overwrite numPlayers aborted")
            }
        }

    var ghost = false

    lateinit var positions: Array<Int>
    lateinit var roles: Array<String>
    lateinit var players: Array<Player>
    lateinit var buttons: List<MaterialButton>
    var voteList = ArrayList<Int>()

    // !DEBUG!
    private val nicknames = arrayOf(
        "Jake Green",
        "Zach",
        "Avi",
        "Doroti Macha",
        "Sorter",
        "Paul",
        "Lord John",
        "Lily Walker",
        "Slim Higgins",
        "Billy",
        "Rachel",
        "Joe",
        "Fred"
    )

    var gameActive = false


    private fun generateRoles() {
        roles = Array(numPlayers) { "CIV" }
        if (numPlayers == 6) {
            roles[0] = "MAF"
        } else {
            roles[0] = "SHR"
            roles[1] = "DON"
            val numMaf = when (numPlayers) {
                in 7..8 -> 2
                in 9..10 -> 3
                in 11..12 -> 4
                else -> 0
            }
            for (i in 2..numMaf) {
                roles[i] = "MAF"
            }
        }
        roles.shuffle()
        Log.i("GameLog", "Roles created: ${roles.joinToString(",")}")


    }

    fun startGame() {
        generateRoles()
        nicknames.shuffle()
        players = Array(numPlayers) { i ->
            Player(
                number = i,
                role = roles[i],
                nickname = nicknames[i]
            )
        }
        Log.i("GameLog", "Game started")
        gameActive = true
    }

    fun endGame() {
        voteList.clear()
        Log.i("GameLog", "Game ended")
        gameActive = false
    }





    fun checkSheriff(id: Int): Boolean {
        val b = players[id].role == "MAF" || players[id].role == "DON"
        Log.i(
            "GameLog", "Player #${players[id].number} (${players[id].role}) " +
                    "hb checked by SHR. Res: ${if (b) "mafia" else "civilian"}"
        )
        return b
    }

    fun checkDon(id: Int): Boolean {
        val b = players[id].role == "SHR"
        Log.i(
            "GameLog", "Player #${players[id].number} (${players[id].role}) " +
                    "hb checked by DON. Res: ${if (b) "SHR" else "not SHR"}"
        )
        return b
    }

    fun addToVoteList(id: Int) {
        if (!voteList.contains(id)) {
            voteList.add(id)
        }
    }

    fun printState() {
        var msg = "Game status: " + if (gameActive) "active\n" else "not active\n"
       msg+="Current action: ${CmdManager.currentIndex}. Actions:\n"
        for(i in 0 until CmdManager.history.size) {
            msg += (if (CmdManager.currentIndex == i+1) '>' else ' ') +
                    (if (i<10) "0$i " else "$i ") +
                    CmdManager.history[i]+'\n'
        }
        msg+="Players:\n"
        for (i in 0 until numPlayers) {
            msg+= players[i].toString()+'\n'
        }
        Log.i("GameLog",msg)
    }

    fun mute(id: Int) {
        Log.i("GameLog", "Player #${players[id].strNum} muted.")
        //TODO mute logic
    }


}