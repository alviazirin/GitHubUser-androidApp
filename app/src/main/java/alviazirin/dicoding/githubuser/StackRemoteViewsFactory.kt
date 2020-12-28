package alviazirin.dicoding.githubuser

import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import alviazirin.dicoding.githubuser.entity.FavUser
import alviazirin.dicoding.githubuser.helper.MappingHelper
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide


internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private lateinit var cursor: Cursor
    private val mWidgetItems = ArrayList<FavUser>()
    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val token = Binder.clearCallingIdentity()
        getFavUser()
        Binder.restoreCallingIdentity(token)
    }

    override fun onDestroy() {
        cursor.close()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {

        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        val bitmap = Glide.with(mContext)
                .asBitmap()
                .load(mWidgetItems.get(position).favUserAvatarUrl)
                .submit(512,512)
                .get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extra = bundleOf(FavUserWidget.EXTRA_ITEM to position)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extra)
        rv.setOnClickFillInIntent(R.id.imageView,fillInIntent)
        return rv

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun getFavUser(){
         cursor = mContext.contentResolver?.query(CONTENT_URI,null,null,null,null) as Cursor
        val favUser = MappingHelper.mapCursorToArrayList(cursor)
        mWidgetItems.addAll(favUser)
    }
}