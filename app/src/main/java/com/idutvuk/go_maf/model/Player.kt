package com.idutvuk.go_maf.model

class Player(
    val number: Int,
    val role: Role = Role.CIV,
    val nickname: String = "Debugger",
//    val isGhost: Boolean = false //TODO: add ghost logic
) {
    var alive = true
    var fouls = 0
    internal val strNum = String.format("%02d", number + 1)
    internal val emoji = when (role) {
        Role.CIV -> "🙂"
        Role.SHR -> "🥸"
        Role.MAF -> "🔫"
        Role.DON -> "💍"
    }

    override fun toString(): String {
        return strNum + emoji + role +(if (alive) "✅alive" else "💀 dead") +
                ", fouls: $fouls. Aka $nickname"
    }
}