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
        /*getImage()*/

       /* val handlerThread = HandlerThread("ObserverDataWidget")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val mObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                getFavUser()
            }
        }
        mContext.contentResolver?.registerContentObserver(CONTENT_URI, true,mObserver)*/
        getFavUser()
        Binder.restoreCallingIdentity(token)
    }

    override fun onDestroy() {
        cursor.close()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
       //getImage()
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
       /* rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])*/
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

    /*private fun getImage(){


        val cursor = mContext.contentResolver?.query(CONTENT_URI,null,null,null,null)
        val favUserAva = MappingHelper.mapCursorToArrayList(cursor)

        for (data in favUserAva){
            Glide.with(mContext)
                    .asBitmap()
                    .load(data.favUserAvatarUrl)
                    .apply(RequestOptions().placeholder(R.drawable.ic_cached_white_24dp))
                    .error(R.drawable.ic_broken_image_white_24dp)
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                            mWidgetItems.add(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                    })
        }
    }*/

    private fun getFavUser(){
         cursor = mContext.contentResolver?.query(CONTENT_URI,null,null,null,null) as Cursor
        val favUser = MappingHelper.mapCursorToArrayList(cursor)
        mWidgetItems.addAll(favUser)
    }
}