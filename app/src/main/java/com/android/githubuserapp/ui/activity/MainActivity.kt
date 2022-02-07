package com.android.githubuserapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuserapp.R
import com.android.githubuserapp.adapter.SearchAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ActivityMainBinding
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: SearchAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
        observe()
    }

    private fun observe() {
        observeSearchUser()
        observeQueryUser()
        observeState()
    }

    private fun observeState() {
        mainViewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        when (state) {
            is MainState.Loading -> showLoading(state.isLoading)
            is MainState.Empty -> showEmptyIllustration(state.isEmpty)
            is MainState.ServerState -> showErrorIllustration(state.isError, state.message)
            is MainState.Recycler -> showRecyclerView(state.isRecycler)
        }
    }

    private fun observeQueryUser() {
        binding.itemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.setSearchData(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun observeSearchUser() {
        mainViewModel.listSearchData.observe(this, { handleSearchUser(it) })
    }

    private fun handleSearchUser(users: ArrayList<User>) {
        binding.recyclerviewSearchUser.adapter.let { user ->
            if (user is SearchAdapter) {
                if (users.size != 0) {
                    user.addData(users)
                    showEmptyIllustration(false)
                    showRecyclerView(true)
                } else {
                    showRecyclerView(false)
                    showEmptyIllustration(true)
                }
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
    }

    private fun setupRecyclerView() {
        mainAdapter = SearchAdapter()

        binding.recyclerviewSearchUser.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(applicationContext)
            val itemDecoration =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
        }

        mainAdapter.setOnItemClickCallBack(object :
            SearchAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(applicationContext, DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    private fun showEmptyIllustration(isEmpty: Boolean) {
        with(binding) {
            mainLayoutIllustrations.root.visibility = if (isEmpty) {
                mainLayoutIllustrations.apply {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_empty)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_state_empty)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_state_empty)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun showErrorIllustration(isError: Boolean, message: String) {
        with(binding) {
            recyclerviewSearchUser.visibility = View.GONE
            mainLayoutIllustrations.root.visibility = if (isError) {
                mainLayoutIllustrations.apply {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_server_status)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_state_server)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_state_serve, message)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun showRecyclerView(isRecycler: Boolean) {
        binding.recyclerviewSearchUser.visibility = if (isRecycler) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.mainProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}