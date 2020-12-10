package alviazirin.dicoding.githubuser.favorite

import alviazirin.dicoding.GitHubUser.R
import alviazirin.dicoding.githubuser.AdapterDB
import alviazirin.dicoding.githubuser.MainViewAdapter
import alviazirin.dicoding.githubuser.db.FavUserHelper
import alviazirin.dicoding.githubuser.detailuser.DetailUserActivity
import alviazirin.dicoding.githubuser.entity.FavUser
import alviazirin.dicoding.githubuser.helper.MappingHelper
import alviazirin.dicoding.githubuser.model.GitHubUserList
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_favorite.progressBar
import kotlinx.android.synthetic.main.activity_favorite.rv_userList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favUserHelper: FavUserHelper
    private lateinit var adapterDB: AdapterDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setAdapterRV()
        favUserHelper = FavUserHelper.getInstance(applicationContext)
        favUserHelper.open()
        loadFavUserFromDB()
        toDetailFav()

    }

    private fun toDetailFav() {
        adapterDB.setOnItemClickCallBack(object : AdapterDB.OnItemClickCallBack {
            override fun OnItemClicked(data: FavUser) {
                val intent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.favUserLoginName)
                startActivity(intent)
            }

        })
    }

    private fun loadFavUserFromDB() {
        GlobalScope.launch(Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
            val defferedUser = async(Dispatchers.IO){
                val cursor = favUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favUser = defferedUser.await()
            if (favUser.size > 0){
                adapterDB.setData(favUser)
            } else {
                val favMessage = resources.getString(R.string.favNoUser)
                showSnackBarMessage(favMessage)
            }
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(rv_userList, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state == true){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

    private fun setAdapterRV(){
        adapterDB = AdapterDB()
        adapterDB.notifyDataSetChanged()

        rv_userList.layoutManager = LinearLayoutManager(this)
        rv_userList.adapter = adapterDB

    }
}