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
import com.android.githubuserapp.databinding.FragmentFollowersBinding
import com.android.githubuserapp.ui.activity.DetailActivity
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var followersAdapter: SearchAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupViewModel()
        observe()

        return binding.root
    }

    private fun observe() {
        observeFollowersData()
        observeQueryLogin()
        observeState()
    }

    private fun observeState() {
        followersViewModel.state.observe(viewLifecycleOwner, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        if (state is MainState.Loading) showLoading(state.isLoading)
    }

    private fun observeQueryLogin() {
        arguments?.getParcelable<User>(EXTRA_USER).let { user ->
            if (user != null) {
                followersViewModel.setFollowersData(user.login)
            }
        }
    }

    private fun observeFollowersData() {
        followersViewModel.listFollowersData.observe(
            viewLifecycleOwner,
            { handleFollowersData(it) })
    }

    private fun handleFollowersData(users: ArrayList<User>) {
        binding.recyclerviewFollowers.adapter.let { user ->
            if (user is SearchAdapter && users.size != 0) {
                user.addData(users)
            }
        }
    }

    private fun setupViewModel() {
        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowersViewModel::class.java]
    }

    private fun setupRecyclerView() {
        followersAdapter = SearchAdapter()

        binding.recyclerviewFollowers.apply {
            adapter = followersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val itemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
        }

        followersAdapter.setOnItemClickCallBack(object :
            SearchAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.followersProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_USER, user)
                }
            }
    }
}