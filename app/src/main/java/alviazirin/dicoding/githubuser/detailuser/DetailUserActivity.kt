package alviazirin.dicoding.githubuser.detailuser

import alviazirin.dicoding.githubuser.R
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.FAVUSERAVATARURL
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.FAVUSERLOGINNAME
import alviazirin.dicoding.githubuser.db.FavUserHelper
import alviazirin.dicoding.githubuser.entity.FavUser
import alviazirin.dicoding.githubuser.helper.MappingHelper
import alviazirin.dicoding.githubuser.ui.tabs.SectionsPagerAdapter
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"


    }

    private lateinit var detailViewModel: DetailUserViewModel
    private var favUser: FavUser? = null
    private var position: Int = 0
    private lateinit var favUserHelper: FavUserHelper
    private var state: Boolean = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val passedUsername = intent.getStringExtra(EXTRA_USERNAME)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        sectionsPagerAdapter.username = passedUsername
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailUserViewModel::class.java)

        showLoading(true)
        if (passedUsername != null) {
            detailViewModel.setDetailUser(passedUsername)

            detailViewModel.getDetailUser().observe(this, Observer { gitHubUserDetailList ->
                if (gitHubUserDetailList != null){
                    val locationNo = getString(R.string.userLocation)
                    val companyNo = getString(R.string.userCompany)
                    val location = gitHubUserDetailList[0].userLocation
                    val company = gitHubUserDetailList[0].userCompany


                    Glide.with(this)
                            .load(gitHubUserDetailList[0].userAvatar)
                            .apply(RequestOptions().override(150,150))
                            .into(iv_DuserAva)
                    tv_DuserLogin.text = gitHubUserDetailList[0].userLoginName
                    tv_DuserRealName.text = gitHubUserDetailList[0].userRealName
                    if (location.equals("null") ){tv_DuserLocation.text = locationNo} else {tv_DuserLocation.text = location}
                    if (company.equals("null") )tv_DuserCompany.text = companyNo else tv_DuserCompany.text = company

                    tv_DuserPubRepo.text = gitHubUserDetailList[0].userPublicRepo.toString()
                    tv_getter.text = gitHubUserDetailList[0].userAvatar


                    showLoading(false)
                }
            })

        }

        favUserHelper = FavUserHelper.getInstance(applicationContext)
        favUserHelper.open()




       loadFavUser(passedUsername.toString())
        var statusFavorite: Boolean = state

        favButton.setOnClickListener { view ->
            statusFavorite = !statusFavorite
            Log.d("statusfavoritenow",statusFavorite.toString())

            if (statusFavorite){

                val userLoginName = tv_DuserLogin.text.toString()
                val userFavAva = tv_getter.text.toString()


                val values = ContentValues()
                values.put(FAVUSERLOGINNAME, userLoginName)
                values.put(FAVUSERAVATARURL, userFavAva)
                contentResolver.insert(CONTENT_URI,values)
                Log.d("statebutton", statusFavorite.toString() + " Doing input DB")


            } else {
                val userLogName = tv_DuserLogin.text.toString()
                val uri= Uri.parse("$CONTENT_URI/$userLogName")
                contentResolver.delete(uri, null, null)
                Log.d("statebutton", statusFavorite.toString() + " Delete data from DB")

            }

            setFavorited(statusFavorite)
            Toast.makeText(this, "Fab button working", Toast.LENGTH_SHORT).show()
        }





    }

    override fun onStart() {
        super.onStart()



    }

    private fun loadFavUser(loginName: String) {


        val uri = Uri.parse("$CONTENT_URI/$loginName")

        val cursor = contentResolver?.query(uri, null, null, null, null)
        val listFav = MappingHelper.mapCursorToArrayList(cursor)




        if (listFav.size>0){
            state = true
            //returnedState = !returnedState
            Log.d("favState", "User is favorited")
            Log.d("stateLoad", state.toString())
            setFavorited(true)
            cursor?.close()
        } else {
            state = false
            //returnedState = !returnedState
            Log.d("favState", "User doesn't favorited")
            Log.d("stateLoad", state.toString())
            setFavorited(false)
            cursor?.close()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state == true){
            progressBarDetail.visibility = View.VISIBLE
        }else{
            progressBarDetail.visibility = View.GONE
        }
    }
    private fun setFavorited(favoriteStatus: Boolean){
        if (favoriteStatus){
            favButton.setImageResource(R.drawable.ic_favorite_white_48dp)
        } else {
            favButton.setImageResource(R.drawable.ic_favorite_border_white_48dp)
        }
    }
}