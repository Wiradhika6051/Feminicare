package com.capstone.feminacare.ui.article

import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.Repository

class ArticleViewModel(private val repository: Repository) : ViewModel() {
    fun getArticle(query: String) = repository.getArticles(query)

}