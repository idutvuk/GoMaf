package com.idutvuk.go_maf.model.gamedata

enum class GamePhase {
    START,

    /**
     * 
     */
    NIGHT,

    /**
     * Players speech, fouls
     */
    SPEECHES,

    /**
     * Day, no speech
     */
    VOTE,

    /**
     * Day.Speech by vote
     */
    VOTE_SPEECHES,

    /**
     * Day. End of the game
     */
    END
}
