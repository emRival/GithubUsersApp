package com.rival.githubusersapp.ui.main.ItemSearchBar.Favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.githubusersapp.Utils.UserDiffCallback
import com.rival.githubusersapp.data.model.FavoriteModel
import com.rival.githubusersapp.databinding.ItemRowUsersBinding
import com.rival.githubusersapp.ui.detail.UserDetailActivity


class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.MyViewHolder>() {
    private val listFavorites = ArrayList<FavoriteModel>()
    fun setListFavorites(favorite: List<FavoriteModel>) {
        val diffCallback = UserDiffCallback(this.listFavorites, favorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(favorite)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(private val binding: ItemRowUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteModel) {
            with(binding) {
                tvUsers.text = user.username
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_USER, user.username)
                    itemView.context.startActivity(intent)
                }
            }
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.ivUsers)
        }
    }

    override fun getItemCount(): Int = listFavorites.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favoriteUser = listFavorites[position]
        holder.bind(favoriteUser)
    }
}