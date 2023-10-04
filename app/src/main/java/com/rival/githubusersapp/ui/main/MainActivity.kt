package com.rival.githubusersapp.ui.main

import UserAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rival.githubusersapp.R
import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.databinding.ActivityMainBinding
import com.rival.githubusersapp.ui.detail.UserDetailActivity
import com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.FavoriteActivity
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingActivity
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingPreferences
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingViewModel
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingViewModelFactory


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingPreferences.getInstance(application.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


      setUpThemes()

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
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        search()
    }

    private fun setUpThemes() {

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
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
            searchBar.inflateMenu(R.menu.searchbar_menu)
            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_favorite -> {
                        val intentFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intentFavorite)
                        true
                    }

                    R.id.menu_preferences -> {
                        val intentSetting = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intentSetting)
                        true
                    }

                    else -> return@setOnMenuItemClickListener true
                }

            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                Toast.makeText(this@MainActivity, "favorite", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menu_preferences -> {
                Toast.makeText(this@MainActivity, "settings", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
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