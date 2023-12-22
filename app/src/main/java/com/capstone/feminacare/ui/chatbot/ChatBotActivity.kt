package com.capstone.feminacare.ui.chatbot

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.feminacare.data.Result
import com.capstone.feminacare.databinding.ActivityChatBotBinding
import com.capstone.feminacare.ui.ViewModelFactory
import com.capstone.feminacare.utils.COOKIES

class ChatBotActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var binding: ActivityChatBotBinding
    private lateinit var adapter: ChatBotMessageAdapter
    private val viewModel by viewModels<ChatBotViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar2)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        setLoadingState()

        viewModel.messages.observe(this) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    adapter.submitList(result.data)
                    if (adapter.itemCount > 0) {
                        recycler.smoothScrollToPosition(adapter.itemCount)
                    }
                }

                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.ibSendMsg.setOnClickListener {
            val message = binding.edtChatInput.text.toString()
            val cookies = intent.getStringExtra(COOKIES)!!
            if (message.isNotEmpty()) viewModel.sendChatbotMessage(message, cookies)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    private fun setupRecyclerView() {
        adapter = ChatBotMessageAdapter()
        recycler = binding.rvChat
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        if (adapter.itemCount > 0) {
            recycler.scrollToPosition(adapter.itemCount)
        }
    }

    private fun setLoadingState() {
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                binding.loadingGenerate.visibility = View.VISIBLE
                binding.edtChatInput.text = null
                binding.edtChatInput.isEnabled = false
            } else {
                binding.loadingGenerate.visibility = View.GONE
                binding.edtChatInput.isEnabled = true
            }
        }
    }

}