package com.idutvuk.go_maf.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.model.database.entities.MafiaGamePlayer
import com.idutvuk.go_maf.model.database.entities.User


@Database(
    entities = [
        MafiaGame::class,
        User::class,
        MafiaGamePlayer::class,
               ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MafiaGamesDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun userDao(): UserDao

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
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}