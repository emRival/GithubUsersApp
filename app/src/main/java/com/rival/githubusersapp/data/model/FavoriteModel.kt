package com.rival.githubusersapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class FavoriteModel(
    @field:PrimaryKey(autoGenerate = false)
    @field:ColumnInfo(name = "login")
    var username: String = "",

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null
) : Parcelable