package com.dicoding.githubconsumer


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubconsumer.db.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import com.dicoding.githubconsumer.detailuser.DetailUserActivity
import com.dicoding.githubconsumer.entity.FavUser
import com.dicoding.githubconsumer.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapterDB: AdapterDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAdapterRV()
        loadFavUserFromDB()
        toDetailFav()
    }

    private fun toDetailFav() {
        adapterDB.setOnItemClickCallBack(object : AdapterDB.OnItemClickCallBack {
            override fun OnItemClicked(data: FavUser) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.favUserLoginName)
                startActivity(intent)
            }

        })
    }


    private fun loadFavUserFromDB() {
        GlobalScope.launch(Dispatchers.Main){
            progressBar.visibility = View.VISIBLE
            val defferedUser = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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