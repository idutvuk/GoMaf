package com.idutvuk.go_maf.model

import android.util.Log
import com.idutvuk.go_maf.model.gameactions.GameAction
import com.idutvuk.go_maf.ui.game.GameFragment

object CmdManager {
    lateinit var gameFragment: GameFragment
    val history = mutableListOf<GameAction>()
    var currentIndex = 0


    fun commit(command: GameAction): IntArray {
        val result = IntArray(3) //[0] for undo state, [1] for redo state, [2] for command code
        result[2] = command.execute()
        if (history.size != currentIndex) {
            repeat(history.size - currentIndex) {
                history.removeLastOrNull()
            }
            result[1] = -1
        }
        history.add(command)
        currentIndex++
        result[0] = 1
        return result
    }

    fun undo(): IntArray {
        val result =  IntArray(2)
        if (currentIndex ==0) {
            Log.e("GameLog", "Attempt to redo null aborted")
            result[0] = -1
            return result
        }
        history[currentIndex -1].undo()
        currentIndex--
        result[1] = 1
        Log.d("GameLog","Undo completed. size: ${history.size}, index:$currentIndex")
        if (currentIndex <=0) {
            result[0] = -1
        }
        return result
    }

    fun redo(): IntArray {
        val result = IntArray(2)
        if (currentIndex >= history.size) {
            Log.e("GameLog", "Attempt to redo null aborted")
            result[1] = -1
            return result
        }
        history[currentIndex].execute()
        Log.i("GameLog", "Action redone")
        currentIndex++
        result[0] = 1
        if (currentIndex >= history.size) {
            result[1] = -1
        }
        return result
    }
}