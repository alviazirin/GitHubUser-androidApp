package alviazirin.dicoding.githubuser.helper

import alviazirin.dicoding.githubuser.db.DatabaseContract
import alviazirin.dicoding.githubuser.entity.FavUser
import android.database.Cursor

object MappingHelper {

    fun mapCursorToArrayList(favUserCursor: Cursor?): ArrayList<FavUser>{
        val favUserList = ArrayList<FavUser>()

        favUserCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavUserColumns._ID))
                val loginName = getString(getColumnIndexOrThrow(DatabaseContract.FavUserColumns.FAVUSERLOGINNAME))
                val userAvaUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavUserColumns.FAVUSERAVATARURL))
                favUserList.add(FavUser(id,loginName,userAvaUrl))
            }
        }
        return favUserList
    }
}