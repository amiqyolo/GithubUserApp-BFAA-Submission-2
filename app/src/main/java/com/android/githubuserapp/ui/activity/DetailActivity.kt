package com.android.githubuserapp.ui.activity

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.android.githubuserapp.R
import com.android.githubuserapp.adapter.SectionPagerAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ActivityDetailBinding
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var user: User

    @StringRes
    private val tabsTitle = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        setupToolbar()
        setupNavigation()
        setupViewModel()
        observe()
    }

    private fun observe() {
        observeDetailUser()
        observeQueryLogin()
        observeState()
    }

    private fun observeState() {
        detailViewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        if (state is MainState.Loading) showLoading(state.isLoading)
    }

    private fun observeQueryLogin() {
        detailViewModel.setDetailData(user.login)
    }

    private fun observeDetailUser() {
        detailViewModel.listDetailData.observe(this, { handleDetailUser(it) })
    }

    private fun handleDetailUser(user: User) {
        binding.apply {
            Glide.with(this@DetailActivity).load(user.avatarUrl).apply(
                RequestOptions().override(96, 96)
            ).error(R.drawable.ic_launcher_foreground)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(ivDetailAvatarUrl)

            tvDetailLogin.text = user.login
            tvDetailName.text = user.name.toString()
            tvDetailCompany.text = user.company.toString()
            tvDetailLocation.text = user.location.toString()
            tvDetailPublicRepos.text = user.publicRepos.toString()

            tabs.getTabAt(0)?.text = getString(R.string.info_follower) + user.followers
            tabs.getTabAt(1)?.text = getString(R.string.info_following) + user.following
        }
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]
    }

    private fun setupNavigation() {
        with(binding) {
            viewPager2.adapter = SectionPagerAdapter(this@DetailActivity, user)
            tabs.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorOnPrimary
                )
            )
            TabLayoutMediator(tabs, viewPager2) { tab, position ->
                tab.text = resources.getString(tabsTitle[position])
            }.attach()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(
                resources.getIdentifier(
                    "R.drawable.ic_arrow_back",
                    "drawable",
                    packageName
                )
            )
            title = resources.getString(R.string.detail_title)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}