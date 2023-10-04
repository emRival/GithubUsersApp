package com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rival.githubusersapp.data.model.FavoriteModel


@Dao
interface FavoriteDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteModel)

    @Query("SELECT * FROM favoritemodel ORDER BY login ASC")
    fun getUser(): LiveData<List<FavoriteModel>>

    @Query("SELECT * FROM favoritemodel WHERE login = :favoriteUser")
    fun getFavoriteUser(favoriteUser: String): LiveData<FavoriteModel>

    @Query("DELETE FROM favoritemodel WHERE login = :favoriteUser")
    fun deleteFavoriteUser(favoriteUser: String)


}