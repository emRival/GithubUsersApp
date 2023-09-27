package com.rival.githubusersapp.ui.main

import UserAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.databinding.ActivityMainBinding
import com.rival.githubusersapp.ui.detail.UserDetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        mainViewModel.user.observe(this) { user ->
            Log.d("rival", user.size.toString())
            setUserData(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        search()
    }

    private fun search() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    mainViewModel.findUser(searchView.text.toString())
                    searchBar.text = searchView.text
                    searchView.hide()
                    false
                }
        }


    }

    override fun onBackPressed() {
        with(binding) {
            if (searchView.isShowing) {
                if (searchView.text.isNullOrBlank()) {
                    mainViewModel.findUser("a")
                    searchBar.text = ""
                    searchView.hide()
                } else {
                    searchView.hide()
                }
            } else {
                finish()
            }
        }

    }


    private fun setUserData(user: List<GithubItemUser>) {
        val adapter = UserAdapter(user)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(
            object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: GithubItemUser) {
                    goToDetail(data)
                }

            }
        )
    }

    private fun goToDetail(data: GithubItemUser) {
        val intentToDetail = Intent(this@MainActivity, UserDetailActivity::class.java)
        intentToDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
        startActivity(intentToDetail)
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbUsers.visibility = View.VISIBLE
        } else {
            binding.pbUsers.visibility = View.INVISIBLE
        }
    }
}