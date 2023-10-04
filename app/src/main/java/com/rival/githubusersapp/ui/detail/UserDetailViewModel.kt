package com.rival.githubusersapp.ui.detail


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rival.githubusersapp.Utils.Event
import com.rival.githubusersapp.data.model.FavoriteModel
import com.rival.githubusersapp.data.model.UserDetailResponse
import com.rival.githubusersapp.data.network.ApiConfig
import com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.repository.FavoriteRepository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {

    private val mUserRepository: FavoriteRepository = FavoriteRepository(application)

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val detail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getFavoriteUser(username: String): LiveData<FavoriteModel> =
        mUserRepository.getFavoriteUser(username)

    fun insert(user: FavoriteModel) {
        mUserRepository.insert(user)
        Log.d("FavoriteAddViewModel", "${user.username}; ${user.avatarUrl} added")
    }

    fun delete(user: String) {
        mUserRepository.delete(user)
    }
    internal fun findDetail(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object  : Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = response.body()
                    } else {
                        _snackbarText.value = Event("onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("onFailure: ${t.message}")
            }

        })
    }
}