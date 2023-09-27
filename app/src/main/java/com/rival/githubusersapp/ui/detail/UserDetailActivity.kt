package com.rival.githubusersapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.rival.githubusersapp.R
import com.rival.githubusersapp.data.model.UserDetailResponse
import com.rival.githubusersapp.databinding.ActivityUserDetailBinding
import com.rival.githubusersapp.ui.detail.fragment.SectionPagerAdapter

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private val mainViewModel: UserDetailViewModel by viewModels()

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


    }

    private fun initializeUserDetail(userLogin: String) {
        mainViewModel.findDetail(userLogin)

        mainViewModel.detail.observe(this) { detailUser ->
            setDetailUser(detailUser)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}