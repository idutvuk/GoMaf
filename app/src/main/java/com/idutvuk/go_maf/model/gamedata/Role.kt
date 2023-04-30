package com.idutvuk.go_maf.model.gamedata

enum class Role(val isRed: Boolean) {
    /**
     * CIV - civilian - Red team
     */
    CIV(true),

    /**
     * MAF - mafia - Black team
     */
    MAF(false),

    /**
     * SHR - sheriff - leader of the red team
     */
    SHR(true),

    /**
     * DON - don - leader of the black team
     */
    DON(false)
}