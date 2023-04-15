package com.idutvuk.go_maf.model

import android.icu.text.TimeZoneFormat.GMTOffsetPatternType

class GameMessage (
    var heading: String = "EMPTY HEADING",
    var description: String = "EMPTY TEXT",
    var importance: Int = -1,
    val tags: List<String> = listOf()
) {
    companion object {
        private var lastMessageId = 0

        /**
         * Generates random action list in debug purpose
         * @deprecated use {@link #getGameActionsList()} instead
         */
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
                    "Action description (it's not there)",
                    CmdManager.history[i].importance)
                )
            }
            return gameMessages
        }
    }

}