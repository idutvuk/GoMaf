package com.idutvuk.go_maf.model.database

data class MafiaAccount( //TODO extract it to individiual database of users
    val id: Int,
    val nickname: String,
    val xp: Int,
    val telegramLink: String
)
