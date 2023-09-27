package com.rival.githubusersapp.ui.detail.fragment


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.databinding.ItemRowUsersBinding

class ListFragmentAdapter(private val listUser: List<GithubItemUser>) :
RecyclerView.Adapter<ListFragmentAdapter.ViewHolder>(){
    class ViewHolder(val binding: ItemRowUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemRowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }


    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = listUser[position]


        Glide.with(holder.itemView.context)
            .load(userData.avatarUrl)
            .into(holder.binding.ivUsers)

        holder.binding.tvUsers.text = userData.login
    }

}