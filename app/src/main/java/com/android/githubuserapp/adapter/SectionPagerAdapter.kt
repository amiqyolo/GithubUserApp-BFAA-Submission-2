package com.android.githubuserapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.ui.fragment.FollowersFragment
import com.android.githubuserapp.ui.fragment.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val user: User) :
    FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment.newInstance(user)
            else -> FollowingFragment.newInstance(user)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

}