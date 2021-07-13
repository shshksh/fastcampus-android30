package com.fastcampus.thoughtoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.thoughtoftheday.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val quoteList = parseQuoteListJson(remoteConfig.getString("quote_list"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuoteListPager(quoteList, isNameRevealed)
            }
        }
    }

    private fun displayQuoteListPager(quoteList: List<Quote>, isNameRevealed: Boolean) {
        binding.viewPager.adapter = QuoteListPagerAdapter(quoteList, isNameRevealed)
    }

    private fun parseQuoteListJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)

        return List<JSONObject>(jsonArray.length()) { jsonArray.getJSONObject(it) }
            .map { Quote(it.getString("quote"), it.getString("name")) }
    }
}
