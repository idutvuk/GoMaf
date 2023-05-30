package com.idutvuk.go_maf.model.gamedata

/**
 * flyweight class for states
 */
data class StateSnapshot (
    val heading: String,
    val description: String,
    val importance: EventImportance
)