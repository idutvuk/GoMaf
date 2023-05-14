package com.idutvuk.go_maf.model

class GameMessage (
    var heading: String = "EMPTY HEADING",
//    var description: String = "EMPTY TEXT",
) {
    companion object {
        private var lastMessageId = 0

        /**
         * Generates random action list in debug purpose
         */
        @Deprecated("Use {@link #getGameActionsList()} instead")
        fun createGameMessagesList(numMessages: Int): ArrayList<GameMessage> {
            val gameMessages = ArrayList<GameMessage>()
            for (i in 1..numMessages) {
                gameMessages.add(GameMessage("Action ${++lastMessageId} heading"))
            }
            return gameMessages
        }

        fun getGameActionsList(): ArrayList<GameMessage> {
            val gameMessages = ArrayList<GameMessage>()
            for (i in 0 until CmdManager.stateHistory.size) {
                gameMessages.add(GameMessage(CmdManager.stateHistory[i].toGameLogString()))
            }
            return gameMessages
        }
    }

}