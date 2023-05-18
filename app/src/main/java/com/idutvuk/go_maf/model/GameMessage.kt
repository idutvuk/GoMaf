package com.idutvuk.go_maf.model

import com.idutvuk.go_maf.ui.game.EventImportance

data class GameMessage (
    var heading: String = "EMPTY HEADING",
    var description: String = "EMPTY TEXT",
    var importance: EventImportance // = EventImportance.REGULAR
)