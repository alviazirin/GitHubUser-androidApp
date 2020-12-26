package alviazirin.dicoding.githubuser.provider

import alviazirin.dicoding.githubuser.db.DatabaseContract.AUTHORITY
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.TABLE_NAME
import alviazirin.dicoding.githubuser.db.FavUserHelper
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class FavUserProvider : ContentProvider() {

    companion object {
        private const val FAVUSER = 1
        private const val FAVUSER_LOGINNAME = 2
        private const val FAVUSER_AVAURL = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favUserHelper: FavUserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVUSER)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAVUSER_LOGINNAME)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/AVAURL", FAVUSER_AVAURL)
        }
    }

    override fun onCreate(): Boolean {
        favUserHelper = FavUserHelper.getInstance(context as Context)
        favUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)){
            FAVUSER -> favUserHelper.queryAll()
            FAVUSER_LOGINNAME -> favUserHelper.queryByLoginName(uri.lastPathSegment.toString())
            FAVUSER_AVAURL -> favUserHelper.queryImgUrl()
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(FAVUSER){
            sUriMatcher.match(uri) -> favUserHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when(FAVUSER_LOGINNAME){
            sUriMatcher.match(uri) -> favUserHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(FAVUSER_LOGINNAME){
            sUriMatcher.match(uri) -> favUserHelper.deleteByLoginName(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

}