package com.idutvuk.go_maf.ui.game

import com.idutvuk.go_maf.model.gamedata.MainBtnState

data class GameUiState (
    val mainBtnState: MainBtnState = MainBtnState.DEBUG,
    val isTimerActive: Boolean = false,
    val cursor: Int? = null
)