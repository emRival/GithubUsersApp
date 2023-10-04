package com.rival.githubusersapp.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.rival.githubusersapp.R
import com.rival.githubusersapp.Utils.ViewModelFactory
import com.rival.githubusersapp.data.model.FavoriteModel
import com.rival.githubusersapp.data.model.UserDetailResponse
import com.rival.githubusersapp.databinding.ActivityUserDetailBinding
import com.rival.githubusersapp.ui.detail.fragment.SectionPagerAdapter
import com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite.FavoriteActivity
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingPreferences
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingViewModel
import com.rival.githubusersapp.ui.main.ItemSearchBar.Setting.SettingViewModelFactory
import com.rival.githubusersapp.ui.main.dataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private val detailViewModel: UserDetailViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingPreferences.getInstance(application.dataStore))
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FRAGMENT = "extra_fragment"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userLogin = intent.getStringExtra(EXTRA_USER)


        initializeUserDetail(userLogin.toString())
        setupTabs(userLogin.toString())


        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = userLogin

        setUpThemes()
    }

    private fun setUpThemes() {

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                binding.tabs.setTabTextColors(resources.getColor(R.color.white), resources.getColor(R.color.white))
                binding.fabFav.rippleColor = resources.getColor(R.color.red)
            } else {
                binding.tabs.setTabTextColors(resources.getColor(R.color.black), resources.getColor(R.color.black))
                binding.fabFav.rippleColor = resources.getColor(R.color.red)
            }
        }
    }

    private fun initializeUserDetail(userLogin: String) {
        detailViewModel.findDetail(userLogin)

        detailViewModel.detail.observe(this) { detailUser ->
            setDetailUser(detailUser)
            setFavoriteUser(detailUser)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setFavoriteUser(detailUser: UserDetailResponse) {
        detailViewModel.getFavoriteUser(detailUser.login ?: "").observe(this) {
            val favoriteUser = FavoriteModel(detailUser.login ?: "", detailUser.avatarUrl)
            var isFavorite = false

            if (it != null) {
                isFavorite = true
                binding.fabFav.setImageResource(R.drawable.ic_favorite_24dp)
            }

            binding.fabFav.setOnClickListener {
                if (!isFavorite) {
                    detailViewModel.insert(favoriteUser)
                    isFavorite = true
                    binding.fabFav.setImageResource(R.drawable.ic_favorite_24dp)
                } else {
                    detailViewModel.delete(favoriteUser.username)
                    isFavorite = false
                    binding.fabFav.setImageResource(R.drawable.favorite_border)
                }
            }
        }

    }

    private fun setupTabs(userLogin: String) {
        val usernameBundle = Bundle()
        usernameBundle.putString(EXTRA_FRAGMENT, userLogin)

        val sectionsPagerAdapter = SectionPagerAdapter(this, usernameBundle)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailUser(detailUser: UserDetailResponse) {
        binding.apply {
            Glide.with(this@UserDetailActivity)
                .load(detailUser.avatarUrl)
                .into(ivDetailPhoto)

            tvDetailName.text = (detailUser.name ?: "").toString()
            tvDetailUsername.text = "@${detailUser.login}"
            tvFollowers.text = getString(R.string.text_followers, detailUser.followers.toString())
            tvFollowing.text = getString(R.string.text_following, detailUser.following.toString())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbUserDetail.visibility = View.VISIBLE
        } else {
            binding.pbUserDetail.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_favorite_detail -> {
                val intentFavorite = Intent(this@UserDetailActivity, FavoriteActivity::class.java)
                startActivity(intentFavorite)
                true
            }
            R.id.menu_share_detail -> {
                detailViewModel.detail.observe(this) {userDetail ->
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey, check out this user! \n${userDetail.htmlUrl}"
                    )
                    shareIntent.type = "text/plain"
                    startActivity(Intent.createChooser(shareIntent, "Share to: "))

                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}