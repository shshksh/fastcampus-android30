package com.fastcampus.bookreview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.bookreview.api.BookService
import com.fastcampus.bookreview.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("85A5CBE064099B82A3AF95B828D0140229337DC24DD6A9DD1C9A153E14E60C40")
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
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }
            })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}