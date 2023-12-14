package com.capstone.feminacare.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.feminacare.R
import com.capstone.feminacare.data.remote.response.ArticleDummy
import com.capstone.feminacare.data.remote.response.Dummy
import com.capstone.feminacare.databinding.FragmentHomeBinding
import com.capstone.feminacare.ui.article.ArticleActivity
import com.capstone.feminacare.ui.main.MainViewModelFactory

class HomeFragment : Fragment(), OnArticleClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        MainViewModelFactory.getInstance()
    }

    private val articleAdapter: ArticleAdapter by lazy {
        ArticleAdapter(this)
    }

    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recycler = binding.rvArticles
        recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycler.adapter = articleAdapter

        viewModel.timeOfDay.observe(viewLifecycleOwner) {
            val greet = getString(R.string.greetings, it)
            binding.tvGreetingTime.text = greet
        }

        getArticles()

        return root
    }

    private fun getArticles() {
        articleAdapter.submitList(Dummy.articles)
//        viewModel.getArticles().observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Result.Loading -> {}
//                is Result.Error -> {
//                    println(result.error)
//                }
//
//                is Result.Success -> {
//                    Log.d("Articles", result.data.news.toString())
//                    articleAdapter.submitList(Dummy.articles)
//                }
//            }
//        }
    }

    override fun onItemClick(article: ArticleDummy) {

        val intent = Intent(context, ArticleActivity::class.java)
        intent.putExtra(ARTICLE_DATA, article)
        println(article)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARTICLE_DATA = "article_data"
    }


}