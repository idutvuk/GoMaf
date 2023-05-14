package com.idutvuk.go_maf.model.gamedata

import com.idutvuk.go_maf.R

enum class GameTime(toString: String, icon: Int) { //TODO: implement toGameLogString and icons (or remove)
    DAY("Day", R.drawable.ic_sun),
    NIGHT("Night", R.drawable.ic_moon);
}
