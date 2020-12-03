package alviazirin.dicoding.githubuser.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavUserColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "favuser"
            const val _ID = "_id"
            const val FAVUSERLOGINNAME = "favuserloginname"
            const val FAVUSERAVATARURL = "favuseravatarurl"
        }
    }
}