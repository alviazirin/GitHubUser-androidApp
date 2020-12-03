package alviazirin.dicoding.githubuseruiuxapi

import alviazirin.dicoding.githubuseruiuxapi.model.GitHubUserList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.github_user_item.view.*

class MainViewAdapter() : RecyclerView.Adapter<MainViewAdapter.MainViewHolder>() {
    private val mData = ArrayList<GitHubUserList>()

        fun setDataMain(items: ArrayList<GitHubUserList>){
            mData.clear()
            mData.addAll(items)
            notifyDataSetChanged()
        }


    inner class MainViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(gitHubUserList: GitHubUserList){
            with(itemView){
                Glide.with(itemView.context)
                    .load(gitHubUserList.avatarUrl)
                    .apply(RequestOptions().override(90,90))
                    .into(iv_userAva)

                tv_userName.text = gitHubUserList.userLoginName


                itemView.setOnClickListener { onItemClickCallBack?.OnItemClicked(gitHubUserList) }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.github_user_item, viewGroup, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun OnItemClicked(data: GitHubUserList)
    }
}