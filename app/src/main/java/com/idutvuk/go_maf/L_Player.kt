package com.idutvuk.go_maf

import android.util.Log

/**
 * Mafia in-game player class.
 * Legacy code, implemented in Game.kt
 */
class L_Player(
    // Private properties
    private var _number: Int = -1,
    private var _nickName: String? = null,
    private var _role: String = "CIV",
    private var _isAlive: Boolean = true,
    private var _fouls: Int = 0
) {
    // Public properties with corresponding getters/setters
    var number: Int
        get() = _number
        set(value) {
            _number = value
        }

    var nickName: String?
        get() = _nickName
        set(value) {
            _nickName = value
        }

    var role: String
        get() = _role
        set(value) {
            _role = value
        }

    var isAlive: Boolean
        get() = _isAlive
        set(value) {
            _isAlive = value
        }

    var fouls: Int
        get() = _fouls
        set(value) {
            _fouls = value
        }


    fun foul() {
        fouls++
        Log.i("GameLog","Player #$_number fouled. Total fouls: $_fouls")
    }

    fun resetFouls() {
        Log.i("GameLog","Player #$_number fouls has been reset")
    }

    override fun toString(): String {
        return "Player#$number, name=$nickName, $role, ${if (isAlive) "alive" else "dead"}, fouls=$fouls)"
    }
}