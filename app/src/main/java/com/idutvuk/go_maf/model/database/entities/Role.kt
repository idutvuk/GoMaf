package com.idutvuk.go_maf.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "role")
class Role (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="role_name")
    val roleName: String,
) {
    companion object {
        val rolesNames = listOf("CIV","SHR","MAF", "DON")
        val roles by lazy {
            List(10) {
                Role(
                    id = it+1,
                    roleName = rolesNames[it],
                )
            }
        }
    }
}