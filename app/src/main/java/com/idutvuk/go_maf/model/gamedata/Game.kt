package com.idutvuk.go_maf.model.gamedata

import android.util.Log

/**
 * Singleton object that holds game status inside of it like positions, roles.
 * May be replaced by the local database
 */
@Deprecated("Use the GameState")
object Game {
    @Deprecated("Use CmdManager")
    var numPlayers = -1
        set(value) {
            if (numPlayers ==-1) {
                field = value
                Log.i("GameLog", "Players number is set as $value")
            } else {
                Log.e("GameLog","Attempt to overwrite numPlayers aborted")
            }
        }

}