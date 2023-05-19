package com.idutvuk.go_maf.ui.gameslist.placeholder

import java.sql.Time
import java.sql.Date
import java.util.Random

object GamesContent {

    val ITEMS: MutableList<GameItem> = ArrayList()

    init {
        val random = Random()

        // Add some sample items.
        for (i in 1..25) {
            val bool = when (random.nextInt(4)) {
                0->null
                1,2->true
                else -> false
            }
            addItem(createPlaceholderItem(i, bool))
        }
    }

    private fun createPlaceholderItem(i: Int, winner: Boolean?): GameItem {
        return GameItem(
            id = i,
            date = Date(2023,12,11),
            time = Time(1,51,32),
            duration = Time(0,41,32),
            numPlayers = 10,
            isWinnerRed = winner
        )
    }

    private fun addItem(item:GameItem) {
        ITEMS.add(item)
    }



    data class GameItem(
        /**
         * @sample 34
         */
        val id: Int,

        /**
         * @sample "04.05.2023"
         */
        val date: Date,

        /**
         * @sample "17:36"
         */
        val time: Time,

        /**
         * Game duration
         * @sample "40:25"
         */
        val duration: Time,

        /**
         * @sample 10
         */
        val numPlayers: Int,

        /**
         * [false] if black
         * [true] if red
         */
        val isWinnerRed: Boolean?

    )
}