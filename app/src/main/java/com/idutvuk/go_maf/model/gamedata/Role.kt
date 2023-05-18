package com.idutvuk.go_maf.model.gamedata

enum class Role(val isRed: Boolean, val emoji: String) {
    /**
     * CIV - civilian - Red team
     */
    CIV(true,"\uD83D\uDE42"),

    /**
     * MAF - mafia - Black team
     */
    MAF(false,"\uD83D\uDD2B"),

    /**
     * SHR - sheriff - leader of the red team
     */
    SHR(true,"\uD83E\uDD78"),

    /**
     * DON - don - leader of the black team
     */
    DON(false,"\uD83D\uDC8D")
}