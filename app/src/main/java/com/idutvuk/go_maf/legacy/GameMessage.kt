package com.idutvuk.go_maf.legacy

import com.idutvuk.go_maf.ui.game.EventImportance
@Deprecated("Use compose")
data class GameMessage (
    var heading: String = "EMPTY HEADING",
    var description: String = "EMPTY TEXT",
    var importance: EventImportance // = EventImportance.REGULAR
)