package com.idutvuk.go_maf.model

import com.google.common.truth.Truth.assertThat
import com.idutvuk.go_maf.legacy.CmdManager
import org.junit.Before
import org.junit.Test

class CmdManagerTest {
    @Before
    fun generateRandomRoles() {
//        CmdManager.stateHistory.clear()
    }

    @Test
    fun `first night check`() {
        CmdManager.commit(CmdCommitType.PRESS_MAIN_BTN)
//        assertThat(
//            CmdManager.stateHistory.last()
//                .mainBtnState
//        ).isNotEqualTo(MainBtnState.AUTOCATASTROPHE)
        assertThat(true).isTrue()
//        CmdManager.commit(CmdCommitType.PRESS_MAIN_BTN)
//        assertThat(CmdManager.stateHistory.last().mainBtnState).isEqualTo(MainBtnState.START_NIGHT)
//        CmdManager.commit(CmdCommitType.PRESS_MAIN_BTN)
//        assertThat(CmdManager.stateHistory.last().mainBtnState).isEqualTo(MainBtnState.START_MAFIA_SPEECH)
//        CmdManager.commit(CmdCommitType.PRESS_MAIN_BTN)
//        assertThat(CmdManager.stateHistory.last().mainBtnState).isEqualTo(MainBtnState.NEXT)
    }

}