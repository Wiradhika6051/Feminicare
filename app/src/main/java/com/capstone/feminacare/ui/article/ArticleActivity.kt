package com.capstone.feminacare.ui.article

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.feminacare.data.remote.response.ArticleDummy
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
        val article = intent.getParcelableExtra<ArticleDummy>(HomeFragment.ARTICLE_DATA) as ArticleDummy
        binding.ivImage.setImageResource(article.imageResources)
        binding.tvContent.text = article.description
        binding.btnTag.text = "Kesehatan Wanita"
        binding.toolBarLayout.title = article.title.limit(40)
        setSupportActionBar(binding.detailToolbar)
        actionBar?.title = article.title.limit(40)
        Log.d("article", article.toString())
    }
}