package alviazirin.dicoding.githubuser



import alviazirin.dicoding.githubuser.detailuser.DetailUserActivity
import alviazirin.dicoding.githubuser.favorite.FavoriteActivity
import alviazirin.dicoding.githubuser.model.GitHubUserList
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MainViewAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAdapterRV()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)


        showLoading(true)
        viewModel.setUser()
        viewModel.getUser().observe(this, Observer { gitHubUserList ->
            if (gitHubUserList!=null){
                adapter.setDataMain(gitHubUserList)
                showLoading(false)
            }
            fab_refresh.setOnClickListener {
                                            adapter.setDataMain(gitHubUserList)
                                            fab_refresh.visibility = View.GONE}
        })

        adapter.setOnItemClickCallBack(object : MainViewAdapter.OnItemClickCallBack {
            override fun OnItemClicked(data: GitHubUserList) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.userLoginName)
                startActivity(intent)
            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
               getSearchedUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                                    val intentFav = Intent(this, FavoriteActivity::class.java)
                                    startActivity(intentFav)
                                }
            R.id.setting -> {
                                    val intentSet = Intent(this, SettingActivity::class.java)
                                    startActivity(intentSet)
                            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state == true){
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun getSearchedUser(user: String){
        searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SearchViewModel::class.java)
        showLoading(true)
        searchViewModel.setSearch(user)
        searchViewModel.getSearch().observe(this, Observer { searchedUser ->
            if (searchedUser != null){
                adapter.setDataMain(searchedUser)
                showLoading(false)
            }

            fab_refresh.visibility = View.VISIBLE
        })
    }

    private fun setAdapterRV(){
        adapter = MainViewAdapter()
        adapter.notifyDataSetChanged()

        rv_userList.layoutManager = LinearLayoutManager(this)
        rv_userList.adapter = adapter
    }

}