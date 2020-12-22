package com.dicoding.githubconsumer.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "alviazirin.dicoding.githubuser"
    const val SCHEME = "content"

    internal class FavUserColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "favuser"
            const val _ID = "_id"
            const val FAVUSERLOGINNAME = "favuserloginname"
            const val FAVUSERAVATARURL = "favuseravatarurl"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}