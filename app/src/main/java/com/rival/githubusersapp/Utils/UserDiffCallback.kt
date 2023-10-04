package com.rival.githubusersapp.Utils

import androidx.recyclerview.widget.DiffUtil
import com.rival.githubusersapp.data.model.FavoriteModel
import com.rival.githubusersapp.data.model.GithubItemUser


class UserDiffCallback(
    private val mOldUserList: List<FavoriteModel>,
    private val mNewUserList: List<FavoriteModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldUserList.size

    override fun getNewListSize(): Int = mNewUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].username == mNewUserList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldUserList[oldItemPosition]
        val newEmployee = mOldUserList[newItemPosition]
        return oldEmployee.avatarUrl == newEmployee.avatarUrl
    }
}