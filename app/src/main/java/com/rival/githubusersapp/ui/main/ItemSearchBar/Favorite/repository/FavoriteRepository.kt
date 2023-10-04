package com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.rival.githubusersapp.data.model.FavoriteModel
import com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.database.FavDatabase
import com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.database.FavoriteDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mUserDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavDatabase.getInstance(application)
        mUserDao = db.favoriteUserDao()
    }

    fun getUser(): LiveData<List<FavoriteModel>> = mUserDao.getUser()

    fun getFavoriteUser(username: String): LiveData<FavoriteModel> =
        mUserDao.getFavoriteUser(username)

    fun insert(favoriteUser: FavoriteModel) {
        executorService.execute { mUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: String) {
        executorService.execute { mUserDao.deleteFavoriteUser(favoriteUser) }
    }
}