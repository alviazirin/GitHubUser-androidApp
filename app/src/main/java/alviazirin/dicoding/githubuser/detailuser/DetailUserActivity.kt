package alviazirin.dicoding.githubuser.detailuser

import alviazirin.dicoding.GitHubUser.R
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.FAVUSERAVATARURL
import alviazirin.dicoding.githubuser.db.DatabaseContract.FavUserColumns.Companion.FAVUSERLOGINNAME
import alviazirin.dicoding.githubuser.db.FavUserHelper
import alviazirin.dicoding.githubuser.entity.FavUser
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import alviazirin.dicoding.githubuser.ui.tabs.SectionsPagerAdapter
import android.content.ContentValues
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        const val EXTRA_FAVUSER = "extra_favuser"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
    }

    private lateinit var detailViewModel: DetailUserViewModel
    private var favUser: FavUser? = null
    private var position: Int = 0
    private lateinit var favUserHelper: FavUserHelper
    private var favivurl: String = "favfailed"

    private var userFavAva: String? = null

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

        favUser = intent.getParcelableExtra(EXTRA_FAVUSER)
        if (favUser != null){
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            favUser = FavUser()
        }

        val userFavAvaUrl = tv_getter.text.toString()
        Log.d("ivdetailsource:", userFavAvaUrl)
        var statusFavorite = false
        setFavorited(statusFavorite)

        favButton.setOnClickListener { view ->
            statusFavorite = !statusFavorite
            if (statusFavorite == true){

                val userLoginName = tv_DuserLogin.text.toString()
                val userFavAva = tv_getter.text.toString()

                val values = ContentValues()
                values.put(FAVUSERLOGINNAME, userLoginName)
                values.put(FAVUSERAVATARURL, userFavAva)

            }
            //kode insert database ditulis disini
            setFavorited(statusFavorite)
            Toast.makeText(this, "Fab button working", Toast.LENGTH_SHORT).show()
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