package com.idutvuk.go_maf.model.gamedata

import android.util.Log
import com.idutvuk.go_maf.model.CmdManager

/**
 * Singleton object that holds game status inside of it like positions, roles.
 * May be replaced by the local database
 */
@Deprecated("Use the GameState")
object Game {
    var numPlayers = -1
        set(value) {
            if (numPlayers ==-1) {
                field = value
                Log.i("GameLog", "Players number is set as $value")
            } else {
                Log.e("GameLog","Attempt to overwrite numPlayers aborted")
            }
        }
    //TODO: Make ghost games possible
}