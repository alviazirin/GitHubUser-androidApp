package alviazirin.dicoding.githubuseruiuxapi

import alviazirin.dicoding.githubuseruiuxapi.model.GitHubUserList
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUser = MutableLiveData<ArrayList<GitHubUserList>>()

    fun setUser(){
        val listUserItem = ArrayList<GitHubUserList>()

        val url = "https://api.github.com/users"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","token 76d86c1b1b1ece5d8c423376cc10287d8431d514")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()){
                        val parsedListUser = responseArray.getJSONObject(i)
                        val gitHubUserList = GitHubUserList()
                        gitHubUserList.id = parsedListUser.getInt("id")
                        gitHubUserList.userLoginName = parsedListUser.getString("login")
                        gitHubUserList.avatarUrl = parsedListUser.getString("avatar_url")

                        listUserItem.add(gitHubUserList)

                        Log.d("Received Data ->" ,
                                "\n Id: ${gitHubUserList.id}," +
                                      "\n LoginName: ${gitHubUserList.userLoginName}" +
                                       "\n Avatar Url: ${gitHubUserList.avatarUrl}")

                    }
                    listUser.postValue(listUserItem)
                } catch (e: Exception){
                    Log.e("Exception Error", e.message.toString())
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
    fun getUser(): LiveData<ArrayList<GitHubUserList>>{
        return listUser
    }
}