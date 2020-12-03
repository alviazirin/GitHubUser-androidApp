package alviazirin.dicoding.githubuser.db

import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.TABLE_NAME
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavuser"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLER_FAVUSER = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavUserColumns._ID} INTEGER PRIMARY KEY AUTO INCREMENT," +
                "${DatabaseContract.FavUserColumns.FAVUSERLOGINNAME} TEXT NOT NULL," +
                "${DatabaseContract.FavUserColumns.FAVUSERAVATARURL} TEXT NOT NULL )"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLER_FAVUSER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}