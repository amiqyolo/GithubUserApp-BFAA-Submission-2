package com.android.githubuserapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuserapp.adapter.SearchAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.FragmentFollowingBinding
import com.android.githubuserapp.ui.activity.DetailActivity
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingAdapter: SearchAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupViewModel()
        observe()

        return binding.root
    }

    private fun observe() {
        observeFollowingData()
        observeQueryLogin()
        observeState()
    }

    private fun observeState() {
        followingViewModel.state.observe(viewLifecycleOwner, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        if (state is MainState.Loading) showLoading(state.isLoading)
    }

    private fun observeQueryLogin() {
        arguments?.getParcelable<User>(EXTRA_USER).let { user ->
            if (user != null) {
                followingViewModel.setFollowingData(user.login)
            }
        }
    }

    private fun observeFollowingData() {
        followingViewModel.listFollowingData.observe(
            viewLifecycleOwner,
            { handleFollowingData(it) })
    }

    private fun handleFollowingData(users: ArrayList<User>) {
        binding.recyclerviewFollowing.adapter.let { user ->
            if (user is SearchAdapter && users.size != 0) {
                user.addData(users)
            }
        }
    }

    private fun setupViewModel() {
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]
    }

    private fun setupRecyclerView() {
        followingAdapter = SearchAdapter()

        binding.recyclerviewFollowing.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val itemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
        }

        followingAdapter.setOnItemClickCallBack(object :
            SearchAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.followingProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_USER, user)
                }
            }
    }
}