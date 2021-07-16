package com.fastcampus.bookreview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.bookreview.adapter.BookAdapter
import com.fastcampus.bookreview.adapter.HistoryAdapter
import com.fastcampus.bookreview.api.BookService
import com.fastcampus.bookreview.databinding.ActivityMainBinding
import com.fastcampus.bookreview.model.BestSellerDto
import com.fastcampus.bookreview.model.History
import com.fastcampus.bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var bookAdapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private val bookService: BookService by lazy { com.fastcampus.bookreview.api.bookService }

    private val db by lazy { getAppDatabase(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        loadBestSeller()
    }

    private fun initBookRecyclerView() {
        bookAdapter = BookAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        }
        binding.rvBookList.adapter = bookAdapter
        binding.rvBookList.layoutManager = LinearLayoutManager(this)
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter { deleteSearchKeyword(it) }
        binding.rvHistory.adapter = historyAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

    }

    private fun initSearchEditText() {
        binding.etSearch.setOnKeyListener { v, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_ENTER) and (event.action == MotionEvent.ACTION_DOWN)) {
                search(binding.etSearch.text.toString())
                return@setOnKeyListener true
            }
            false
        }
        binding.etSearch.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            false
        }
    }

    private fun loadBestSeller() {
        bookService.getBestSellerBooks(getString(R.string.interparkAPIKey))
            .enqueue(object : Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not())
                        return

                    response.body()?.let {
                        bookAdapter.submitList(it.bookList)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }
            })
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interparkAPIKey), keyword)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    if (response.isSuccessful.not())
                        return

                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    bookAdapter.submitList(response.body()?.bookList.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                    hideHistoryView()
                }
            })
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.rvHistory.isVisible = true
                historyAdapter.submitList(keywords)
            }
        }.start()
        binding.rvHistory.isVisible = true
    }

    private fun hideHistoryView() {
        binding.rvHistory.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}