package com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rival.githubusersapp.Utils.ViewModelFactory
import com.rival.githubusersapp.databinding.ActivityFavoriteBinding
import com.rival.githubusersapp.ui.detail.UserDetailViewModel


class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteUserAdapter
    private val favViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite User"

        favViewModel.getUser().observe(this) { listFavoriteUser ->
            if (listFavoriteUser != null) {
                adapter.setListFavorites(listFavoriteUser)
            }
        }

        adapter = FavoriteUserAdapter()
        binding.rvFavoriteUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.adapter = adapter
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