package com.idutvuk.go_maf.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idutvuk.go_maf.model.database.MafiaAccount.Companion.users
import java.sql.Date
import java.sql.Time

/*
Table user {

  username varchar
  telegram_id varchar
}
 */

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="username")
    val username: String,

    @ColumnInfo(name="telegram_id")
    val telegramId: String,

) {
    companion object {
        val usernames = listOf("johndoe", "janedoe", "bobsmith", "sarahjones", "michaelbrown", "emilydavis", "adamwilson", "oliviamiller", "davidlee", "laurenwright")
        val telegramIds = listOf("123456789", "987654321", "2468101214", "369121518", "1514131211", "1817161413", "2019181716", "2322212019", "2625242321", "2928272624")
        val users by lazy {
            List(10) {
                User(
                    id = it+1,
                    username = usernames[it],
                    telegramId = telegramIds[it]
                )
            }
        }

    }
}
