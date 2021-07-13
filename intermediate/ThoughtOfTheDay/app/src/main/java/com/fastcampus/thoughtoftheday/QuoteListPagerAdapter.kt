package com.fastcampus.thoughtoftheday

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fastcampus.thoughtoftheday.databinding.ItemQuoteBinding

class QuoteListPagerAdapter(
    private val quoteList: List<Quote>,
    private val isNameRevealed: Boolean
) : RecyclerView.Adapter<QuoteListPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val quoteBinding =
            ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(quoteBinding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quoteList.size
        holder.bind(quoteList[actualPosition], isNameRevealed)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameRevealed: Boolean) {
            with(binding) {
                tvQuote.text = """"${quote.quote}""""
                if (isNameRevealed) {
                    tvName.text = "- ${quote.name}"
                    tvName.visibility = View.VISIBLE
                } else {
                    tvName.visibility = View.GONE
                }
            }
        }
    }
}