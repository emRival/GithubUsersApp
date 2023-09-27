package com.rival.githubusersapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rival.githubusersapp.Utils.Event
import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.data.model.GithubResponse
import com.rival.githubusersapp.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {
    companion object {
        val TAG = "MainActivity"
    }

    private val _user = MutableLiveData<List<GithubItemUser>>()
    val user: LiveData<List<GithubItemUser>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        findUser("rival")
    }

    internal fun findUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _user.value = response.body()?.items
                    } else {
                        _isLoading.value = false
                        _snackbarText.value = Event("onFailure: ${response.message()}")
                    }

                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("onFailure: ${t.message}")
            }

        })

    }
}