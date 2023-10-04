package com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rival.githubusersapp.data.model.FavoriteModel


@Database(entities = [FavoriteModel::class], version = 2)
abstract class FavDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): FavDatabase {
            if (INSTANCE == null) {
                synchronized(FavDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavDatabase::class.java,
                        "FavoriteUser"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as FavDatabase
        }
    }
}