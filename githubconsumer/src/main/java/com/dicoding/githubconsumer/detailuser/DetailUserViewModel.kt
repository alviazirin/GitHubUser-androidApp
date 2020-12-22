package com.dicoding.githubconsumer.detailuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubconsumer.model.GitHubUserDetailList
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUserViewModel : ViewModel() {


    val detailUser = MutableLiveData<ArrayList<GitHubUserDetailList>>()
    var toastMessageObserver = MutableLiveData<String>()


    fun setDetailUser(passedUsername: String){
        val detailUserItem = ArrayList<GitHubUserDetailList>()

        val url = "https://api.github.com/users/$passedUsername"

        val clientDetail = AsyncHttpClient()
        clientDetail.addHeader("Authorization","token 76d86c1b1b1ece5d8c423376cc10287d8431d514")
        clientDetail.addHeader("User-Agent","request")
        clientDetail.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val response = JSONObject(result)
                    val gitHubUserDetailList = GitHubUserDetailList()
                    gitHubUserDetailList.id = response.getInt("id")
                    gitHubUserDetailList.userAvatar = response.getString("avatar_url")
                    gitHubUserDetailList.userLoginName = response.getString("login")
                    gitHubUserDetailList.userRealName = response.getString("name")
                    gitHubUserDetailList.userLocation = response.getString("location")
                    gitHubUserDetailList.userCompany = response.getString("company")
                    gitHubUserDetailList.userPublicRepo = response.getInt("public_repos")

                    detailUserItem.add(gitHubUserDetailList)


                    detailUser.postValue(detailUserItem)
                } catch (e: Exception){
                    Log.e("getDataExcept", e.message.toString())
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.e("onFailure", error.message.toString())
                error.printStackTrace()
            }

        })


    }
    fun getDetailUser(): LiveData<ArrayList<GitHubUserDetailList>> {
        return detailUser
    }




    }


