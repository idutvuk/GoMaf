package com.idutvuk.go_maf.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [MafiaGame::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MafiaGamesDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var Instance: MafiaGamesDatabase? = null

        fun getDatabase(context: Context): MafiaGamesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MafiaGamesDatabase::class.java,
                    "games"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}