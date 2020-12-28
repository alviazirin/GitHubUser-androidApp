package alviazirin.dicoding.githubuser


import alviazirin.dicoding.githubuser.entity.FavUser
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.github_user_item.view.*

class AdapterDB() : RecyclerView.Adapter<AdapterDB.AdapterDBViewHolder>() {
    private val mData = ArrayList<FavUser>()

    fun setData(items: ArrayList<FavUser>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class AdapterDBViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser: FavUser){
            with(itemView){
                Glide.with(itemView.context)
                    .load(favUser.favUserAvatarUrl)
                    .apply(RequestOptions().override(90,90))
                    .into(iv_userAva)
                tv_userName.text = favUser.favUserLoginName

                itemView.setOnClickListener { onItemClickCallBack?.OnItemClicked(favUser) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDBViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.github_user_item, parent, false)
        return AdapterDBViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterDBViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    private var onItemClickCallBack: AdapterDB.OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun OnItemClicked(data: FavUser)
    }
}
