package com.dicoding.githubconsumer.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavUser(
    var id: Int = 0,
    var favUserLoginName: String? = null,
    var favUserAvatarUrl: String? = null
) : Parcelable
