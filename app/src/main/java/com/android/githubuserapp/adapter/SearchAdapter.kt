package com.android.githubuserapp.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuserapp.R
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ItemRowGithubUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

@SuppressLint("NotifyDataSetChanged")
class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {

    private lateinit var onItemClickCallback: OnItemClickCallBack
    private var items: ArrayList<User> = arrayListOf()
    private var itemsFiltered: ArrayList<User> = arrayListOf()
    private var lastFilter: String = ""

    fun addData(items: ArrayList<User>) {
        this.items.clear()
        this.items = items
        filter.filter(lastFilter)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: User)
    }

    inner class SearchViewHolder(private val binding: ItemRowGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun User.bind() {
            binding.apply {
                Glide.with(itemView.context).load(avatarUrl).apply(
                    RequestOptions().override(56, 56)
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
                    }).into(imgItemAvatar)

                tvItemLogin.text = login
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        return SearchViewHolder(
            ItemRowGithubUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        with(holder) {
            val user: User = itemsFiltered[position]
            user.bind()
            itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
        }
    }

    override fun getItemCount(): Int = itemsFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                itemsFiltered.clear()

                if (charSequence.toString().isNotEmpty()) {
                    items.filter {
                        (it.login.contains(charSequence.toString().lowercase()))
                    }
                        .forEach { itemsFiltered.add(it) }
                } else {
                    itemsFiltered.addAll(items)
                }

                lastFilter = charSequence.toString()
                return FilterResults().apply { values = itemsFiltered }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                charSequence: CharSequence?,
                filterResult: FilterResults?,
            ) {
                (filterResult?.values as ArrayList<User>).also { itemsFiltered = it }
                notifyDataSetChanged()
            }
        }
    }
}