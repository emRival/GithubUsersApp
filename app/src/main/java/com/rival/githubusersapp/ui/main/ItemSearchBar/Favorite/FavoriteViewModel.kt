package com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rival.githubusersapp.data.model.FavoriteModel
import com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserRepository: FavoriteRepository = FavoriteRepository(application)

    fun getUser(): LiveData<List<FavoriteModel>> = mUserRepository.getUser()
}