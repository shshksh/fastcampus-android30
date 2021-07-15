package com.fastcampus.bookreview

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interparkAPIKey))
            .enqueue(object : Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not())
                        return

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.bookList.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        adapter.submitList(it.bookList)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }
            })

    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()
        binding.rvBookList.adapter = adapter
        binding.rvBookList.layoutManager = LinearLayoutManager(this)
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter { deleteSearchKeyword(it) }
        binding.rvHistory.adapter = historyAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this)

        initSearchEditText()
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

                    adapter.submitList(response.body()?.bookList.orEmpty())
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