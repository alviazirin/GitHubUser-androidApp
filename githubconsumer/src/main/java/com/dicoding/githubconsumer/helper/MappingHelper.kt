package com.dicoding.githubconsumer.helper

import android.database.Cursor
import com.dicoding.githubconsumer.db.DatabaseContract
import com.dicoding.githubconsumer.entity.FavUser

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