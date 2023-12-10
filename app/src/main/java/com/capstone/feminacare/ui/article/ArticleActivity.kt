package com.capstone.feminacare.ui.article

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.feminacare.data.remote.response.NewsItem
import com.capstone.feminacare.databinding.ActivityArticleBinding
import com.capstone.feminacare.ui.main.ui.home.HomeFragment
import com.capstone.feminacare.utils.StringUtils.limit

class ArticleActivity : AppCompatActivity() {
    private val binding: ActivityArticleBinding by lazy {
        ActivityArticleBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupData()

    }

    @Suppress("DEPRECATION")
    private fun setupData() {
        val article = intent.getParcelableExtra<NewsItem>(HomeFragment.ARTICLE_DATA) as NewsItem
        Glide.with(applicationContext).load(article.image).into(binding.ivImage)
        binding.tvContent.text = article.summary
        binding.btnTag.text = article.source
        binding.toolBarLayout.title = article.title.limit(40)
        setSupportActionBar(binding.detailToolbar)
        actionBar?.title = article.title.limit(40)
        Log.d("article", article.toString())
    }
}