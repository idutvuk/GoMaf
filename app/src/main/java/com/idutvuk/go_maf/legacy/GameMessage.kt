package com.idutvuk.go_maf.legacy

import com.idutvuk.go_maf.model.gamedata.EventImportance
@Deprecated("Use compose")
data class GameMessage (
    var heading: String = "EMPTY HEADING",
    var description: String = "EMPTY TEXT",
    var importance: EventImportance // = EventImportance.REGULAR
)