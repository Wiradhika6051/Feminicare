package com.capstone.feminacare.ui.main.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.feminacare.data.remote.response.NewsItem
import com.capstone.feminacare.databinding.ItemArticleBinding
import com.capstone.feminacare.utils.TimeUtils

class ArticleAdapter (
    private val onClick: OnArticleClickListener
) : ListAdapter<NewsItem, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.title== newItem.title
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem

            }
        }
    }

    inner class ArticleViewHolder(val binding : ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("PrivateResource")
        fun bind(article: NewsItem){
            Glide.with(binding.root)
                .load(article.image)
                .error(com.google.android.material.R.drawable.mtrl_ic_error)
                .into(binding.ivArticle)

            binding.tvTitle.text = article.title
            binding.tvTag.text = "Health"
            binding.tvTime.text = TimeUtils.getTimeAgo(article.publishedOn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)

        holder.itemView.setOnClickListener {
            onClick.onItemClick(article)
        }
        holder.bind(article)
    }
}

interface OnArticleClickListener {
    fun onItemClick(article: NewsItem)
}