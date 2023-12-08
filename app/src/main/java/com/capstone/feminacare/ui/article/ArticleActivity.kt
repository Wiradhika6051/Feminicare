package com.capstone.feminacare.ui.article

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.feminacare.data.remote.response.NewsItem
import com.capstone.feminacare.databinding.ActivityArticleBinding
import com.capstone.feminacare.ui.main.ui.home.HomeFragment

class ArticleActivity : AppCompatActivity() {
    private val binding: ActivityArticleBinding by lazy {
        ActivityArticleBinding.inflate(layoutInflater)
    }
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val article = intent.getParcelableExtra<NewsItem>(HomeFragment.ARTICLE_DATA)

        Log.d("article", article.toString())
    }
}