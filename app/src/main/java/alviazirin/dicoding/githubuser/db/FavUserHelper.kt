package alviazirin.dicoding.githubuser.db

import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.FAVUSERLOGINNAME
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.TABLE_NAME
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion._ID
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class FavUserHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: FavUserHelper? = null
        fun getInstance(context: Context): FavUserHelper = INSTANCE ?: synchronized(this){
            INSTANCE ?: FavUserHelper(context)
        }

        private lateinit var database: SQLiteDatabase
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        database = dataBaseHelper.writableDatabase
    }

    fun close(){
        dataBaseHelper.close()

        if (database.isOpen){
            database.close()
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryByLoginName(userLoginName: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$FAVUSERLOGINNAME = ?",
            arrayOf(userLoginName),
            null,
            null,
            null,
            null
        )
    }

    fun insert (values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update (userLoginName: String, values: ContentValues?): Int{
        return database.update(DATABASE_TABLE, values, "$FAVUSERLOGINNAME = ?", arrayOf(userLoginName))
    }

    fun deleteByLoginName (userLoginName: String): Int{
        return database.delete(DATABASE_TABLE, "$FAVUSERLOGINNAME = '$userLoginName'", null)
    }
}