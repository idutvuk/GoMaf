package com.idutvuk.go_maf.model.gameactions

import android.util.Log
import com.idutvuk.go_maf.model.Game

class AddToVoteAction(private val nominatedId: Int, private val nominaterId: Int) : GameAction {
    override fun execute(): Int {
        if (Game.voteList.contains(nominatedId)) {
            Log.e("GameLog",
                "Attempt to add person to the voting aborted (already nominated)")
            return -1
        }
        Game.voteList.add(nominatedId)
        Log.i("GameLog","Player #${Game.players[nominatedId].strNum} was nominated by " +
                "player #${Game.players[nominaterId].strNum}")
        return 0
    }

    override fun undo(){
        if(!Game.voteList.remove(nominatedId)) {
            Log.wtf("GameLog","Failed attempt to remove unexisting candidate to voting\n" +
                    "Details: nominated: $nominatedId, vote list: ${Game.voteList.joinToString(",") }}")
        }
    }

    override fun toString(): String {
        return "nominated ${Game.players[nominatedId].strNum} by ${Game.players[nominaterId].strNum}"
    }
}