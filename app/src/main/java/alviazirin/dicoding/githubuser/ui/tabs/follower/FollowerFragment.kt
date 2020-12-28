package alviazirin.dicoding.githubuser.ui.tabs.follower

import alviazirin.dicoding.githubuser.MainViewAdapter
import alviazirin.dicoding.githubuser.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {



    companion object{

        private val ARG_USERNAME = "username"
    }

    private lateinit var adapter: MainViewAdapter
    private lateinit var followerViewModel: FollowerViewModel

    fun newInstance(username: String): FollowerFragment {
        val fragment = FollowerFragment()
        val bundle = Bundle()
        bundle.putString(ARG_USERNAME, username)
        fragment.arguments = bundle
        return  fragment
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setAdapterRV()

        val passedUsername = arguments?.getString(ARG_USERNAME)

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerViewModel::class.java)
        showLoading(true)
        followerViewModel.setFollower(passedUsername.toString())
        followerViewModel.getFollower().observe(viewLifecycleOwner, Observer { followerList ->
            if (followerList != null){
                adapter.setDataMain(followerList)
                showLoading(false)
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state == true){
            progressBarFollower.visibility = View.VISIBLE
        }else{
            progressBarFollower.visibility = View.GONE
        }
    }

    private fun setAdapterRV(){
        adapter = MainViewAdapter()
        adapter.notifyDataSetChanged()

        rv_follower.layoutManager = LinearLayoutManager(context)
        rv_follower.adapter = adapter

    }

}