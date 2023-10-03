package com.rival.githubusersapp.ui.detail.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.data.network.ApiConfig
import com.rival.githubusersapp.ui.main.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentViewModel : ViewModel() {

    private val _userFollowers = MutableLiveData<List<GithubItemUser>>()
    val followers: LiveData<List<GithubItemUser>> = _userFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    internal fun findFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object  : Callback<List<GithubItemUser>> {
            override fun onResponse(
                call: Call<List<GithubItemUser>>,
                response: Response<List<GithubItemUser>>
            ) {

                if (response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
//                        Log.d("ZZ_FRAG", responseBody.toString())
                        _userFollowers.value = response.body()
                    } else {
//                        _isLoading.value = false
                        Log.e(MainViewModel.TAG, "Detail onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<GithubItemUser>>, t: Throwable) {
                _isLoading.value = false
                Log.e(MainViewModel.TAG, "Detail onFailure: ${t.message}")
            }

        })
    }

    internal fun findFollowings(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object  : Callback<List<GithubItemUser>> {
            override fun onResponse(
                call: Call<List<GithubItemUser>>,
                response: Response<List<GithubItemUser>>
            ) {

                if (response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
//                        Log.d("ZZ_FRAG", responseBody.toString())
                        _userFollowers.value = response.body()
                    } else {
//                        _isLoading.value = false
                        Log.e(MainViewModel.TAG, "Detail onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<GithubItemUser>>, t: Throwable) {
                _isLoading.value = false
                Log.e(MainViewModel.TAG, "Detail onFailure: ${t.message}")
            }

        })
    }
}