package alviazirin.dicoding.GitHubUser.ui.tabs.following

import alviazirin.dicoding.GitHubUser.model.GitHubUserList
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<GitHubUserList>>()

    fun setFollowing(passedUsername: String){
        val listFollowingItem = ArrayList<GitHubUserList>()

        val url = "https://api.github.com/users/$passedUsername/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","token 76d86c1b1b1ece5d8c423376cc10287d8431d514")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val response = JSONArray(result)
                    for (i in 0 until response.length()){
                        val parsedListFollowing = response.getJSONObject(i)
                        val followingList = GitHubUserList()
                        followingList.id = parsedListFollowing.getInt("id")
                        followingList.userLoginName = parsedListFollowing.getString("login")
                        followingList.avatarUrl = parsedListFollowing.getString("avatar_url")

                        listFollowingItem.add(followingList)
                    }
                    listFollowing.postValue(listFollowingItem)
                } catch (e: Exception){
                    Log.e("getFollowing", e.message.toString())
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.e("onFailure", error.message.toString())
                error.printStackTrace()
            }

        })
    }
    fun getFollowing(): LiveData<ArrayList<GitHubUserList>> {
        return listFollowing
    }



}