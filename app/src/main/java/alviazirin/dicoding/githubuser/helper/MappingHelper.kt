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

    fun mapCursorToObject(favCursor: Cursor?):FavUser{
        var favUser = FavUser()
        favCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavUserColumns._ID))
            val userLoginName = getString(getColumnIndexOrThrow(DatabaseContract.FavUserColumns.FAVUSERLOGINNAME))
            val userAva = getString(getColumnIndexOrThrow(DatabaseContract.FavUserColumns.FAVUSERAVATARURL))
            favUser = FavUser(id,userLoginName,userAva)
        }
        return  favUser
    }
}