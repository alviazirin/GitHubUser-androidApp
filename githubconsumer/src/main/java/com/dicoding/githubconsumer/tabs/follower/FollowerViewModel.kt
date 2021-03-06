package alviazirin.dicoding.githubuser.ui.tabs.follower


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubconsumer.model.GitHubUserList
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerViewModel : ViewModel() {

    val listFollower = MutableLiveData<ArrayList<GitHubUserList>>()


    fun setFollower(passedUsername: String){

        val listFollowerItem = ArrayList<GitHubUserList>()

        val url = "https://api.github.com/users/$passedUsername/followers"

        val clientFollower = AsyncHttpClient()
        clientFollower.addHeader("Authorization","token 76d86c1b1b1ece5d8c423376cc10287d8431d514")
        clientFollower.addHeader("User-Agent","request")
        clientFollower.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val response = JSONArray(result)
                    for (i in 0 until response.length()){
                        val parsedListFollower = response.getJSONObject(i)
                        val followerList = GitHubUserList()
                        followerList.id = parsedListFollower.getInt("id")
                        followerList.userLoginName = parsedListFollower.getString("login")
                        followerList.avatarUrl = parsedListFollower.getString("avatar_url")

                        listFollowerItem.add(followerList)

                    }
                    listFollower.postValue(listFollowerItem)
                } catch (e: Exception){
                    Log.d("getFollowerException", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.e("onFailure", error.message.toString())
                error.printStackTrace()
            }

        })
    }
    fun getFollower(): MutableLiveData<ArrayList<GitHubUserList>> {
        return listFollower
    }


}