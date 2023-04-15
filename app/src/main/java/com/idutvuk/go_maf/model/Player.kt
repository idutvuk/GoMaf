package com.idutvuk.go_maf.model

class Player(
    val number: Int,
    val role: String = "CIV", //TODO: extract to ENUM
    val nickname: String = "Debugger",
//    val isGhost: Boolean = false //TODO: add ghost logic
) {
    var alive = true
    var fouls = 0
    internal val strNum = String.format("%02d", number + 1)
    internal val emoji = when (role) {
        "CIV" -> "🙂"
        "SHR" -> "🥸"
        "MAF" -> "🔫"
        "DON" -> "💍"
        else -> {
            "🤷‍♂️IDK "
        }
    }

    override fun toString(): String {
        return strNum + emoji + role +(if (alive) "✅alive" else "💀 dead") +
                ", fouls: $fouls. Aka $nickname"
    }
}