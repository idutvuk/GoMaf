package com.idutvuk.go_maf.model

import android.icu.text.TimeZoneFormat.GMTOffsetPatternType

class GameMessage (
    var heading: String = "EMPTY HEADING",
    var description: String = "EMPTY TEXT",
    val tags: List<String> = listOf()
) {
    companion object {
        private var lastMessageId = 0
        fun createGameMessagesList(numMessages: Int): ArrayList<GameMessage> {
            val gameMessages = ArrayList<GameMessage>()
            for (i in 1..numMessages) {
                gameMessages.add(GameMessage("Action ${++lastMessageId} heading", "text"))
            }
            return gameMessages
        }

        fun getGameActionsList(): ArrayList<GameMessage> {
            val gameMessages = ArrayList<GameMessage>()
            for (i in 0 until CmdManager.history.size) {
                gameMessages.add(GameMessage(CmdManager.history[i].toString(),
                    "Action description (it's not there)"))
            }
            return gameMessages
        }
    }

}