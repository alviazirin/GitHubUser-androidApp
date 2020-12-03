package alviazirin.dicoding.githubuseruiuxapi.ui.tabs.following

import alviazirin.dicoding.githubuseruiuxapi.MainViewAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import alviazirin.dicoding.githubuseruiuxapi.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*


class FollowingFragment : Fragment() {

    private lateinit var adapter: MainViewAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object{
        private  val ARG_USERNAME = "username"
    }

    fun newInstance(username: String): FollowingFragment {
        val fragment = FollowingFragment()
        val bundle = Bundle()
        bundle.putString(ARG_USERNAME, username)
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setAdapterRV()

        val passedUsername = arguments?.getString(ARG_USERNAME)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        showLoading(true)
        followingViewModel.setFollowing(passedUsername.toString())
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingList ->
            if (followingList != null){
                adapter.setDataMain(followingList)
                showLoading(false)
            }
        })


    }
    private fun showLoading(state: Boolean) {
        if (state == true){
            progressBarFollowing.visibility = View.VISIBLE
        }else{
            progressBarFollowing.visibility = View.GONE
        }
    }

    private fun setAdapterRV(){
        adapter = MainViewAdapter()
        adapter.notifyDataSetChanged()

        rv_following.layoutManager = LinearLayoutManager(context)
        rv_following.adapter = adapter

    }
}