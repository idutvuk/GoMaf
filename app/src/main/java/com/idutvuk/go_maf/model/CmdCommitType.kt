package com.idutvuk.go_maf.model

import com.idutvuk.go_maf.model.gamedata.commitstates.CmdCommitState
import com.idutvuk.go_maf.model.gamedata.commitstates.PressMainBtn
import com.idutvuk.go_maf.model.gamedata.commitstates.PressPlayerNumber
import com.idutvuk.go_maf.model.gamedata.commitstates.SkipDay
import com.idutvuk.go_maf.model.gamedata.commitstates.SkipNight

enum class CmdCommitType(val cmdCommitState: CmdCommitState) { //TODO: rename @ALL
    SKIP_NIGHT(SkipNight()),
    SKIP_DAY(SkipDay()),
    PRESS_PLAYER_NUMBER(PressPlayerNumber()),
    PRESS_MAIN_BTN(PressMainBtn());

}
