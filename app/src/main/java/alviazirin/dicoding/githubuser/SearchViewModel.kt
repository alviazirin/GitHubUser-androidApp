package alviazirin.dicoding.githubuser

import alviazirin.dicoding.githubuser.model.GitHubUserList
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class SearchViewModel : ViewModel() {

    val listSearch = MutableLiveData<ArrayList<GitHubUserList>>()

    fun setSearch(searchedUser: String){
        val listSearchItem = ArrayList<GitHubUserList>()
        val url = "https://api.github.com/search/users?q=$searchedUser"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","token 76d86c1b1b1ece5d8c423376cc10287d8431d514")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val response = JSONObject(result)
                    val items = response.getJSONArray("items")
                    for (i in 0 until items.length()){
                        val users = items.getJSONObject(i)
                        val resSearchedUser = GitHubUserList()
                        resSearchedUser.id = users.getInt("id")
                        resSearchedUser.userLoginName = users.getString("login")
                        resSearchedUser.avatarUrl = users.getString("avatar_url")
                        listSearchItem.add(resSearchedUser)

                    }
                    listSearch.postValue(listSearchItem)
                }catch (e: Exception){
                    Log.e("searchException", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
               Log.e("onFailure",  error.message.toString())
                error.printStackTrace()
            }

        })

    }

    fun getSearch (): LiveData<ArrayList<GitHubUserList>> {
        return listSearch
    }

}