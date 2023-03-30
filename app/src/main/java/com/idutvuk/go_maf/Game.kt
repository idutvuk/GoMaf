package com.idutvuk.go_maf

import android.util.Log

object Game {
    const val minPlayers = 6
    const val maxPlayers = 12
    var playerNumber = 10
        set(value) {
            field = value
            Log.i("GameLog", "Players number is set as $value")
        }

    var ghost = false

    lateinit var positions: Array<Int>
    lateinit var roles: Array<String>
    lateinit var players: Array<Player>
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


    class Player(
        val number: Int,
        val role: String = "CIV",
        val nickname: String = "Debugger",
        val isGhost: Boolean = false
    ) {
        var alive = true
        var fouls = 0
        var isAddedForVoting = false


        override fun toString(): String {
            return String.format("%02d", number + 1) +
                    when(role) {
                        "CIV" -> " 🙂CIV "
                        "SHR" -> " 🥸SHR "
                        "MAF" -> " 🔫MAF "
                        "DON" -> " 💍DON "
                        else -> {" 🤷‍♂️IDK "}
                    } + (if (alive) "✅alive" else "💀 dead") +
                    ", fouls: $fouls. Aka $nickname"
        }
    }


    private fun generateRoles() {
        roles = Array(playerNumber) { "CIV" }
        if (playerNumber == 6) {
            roles[0] = "MAF"
        } else {
            roles[0] = "SHR"
            roles[1] = "DON"
            val numMaf = when (playerNumber) {
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
        players = Array(playerNumber) { i ->
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

    fun kill(id: Int) {
        if (!players[id].alive) {
            Log.w("GameLog", "Attempt to kill dead person. Aboring")
            return
        }
        players[id].alive = false
        Log.i("GameLog", "Player #${players[id].number} hb killed")
        //TODO kill logic
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

    fun getState() {
        Log.i("GameLog", "Game status: " + if (gameActive) "active" else "not active")
        for (i in 0 until playerNumber) {
            Log.i("GameLog", players[i].toString())
        }
    }
}