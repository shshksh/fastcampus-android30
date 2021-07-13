package com.fastcampus.thoughtoftheday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fastcampus.thoughtoftheday.databinding.ItemQuoteBinding

class QuotesPagerAdapter(
    private val quoteList: List<Quote>
) : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val quoteBinding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context))
        return QuoteViewHolder(quoteBinding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quoteList[position])
    }

    override fun getItemCount(): Int = quoteList.size

    class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: Quote) {
            binding.tvQuote.text = quote.quote
            binding.tvName.text = quote.name
        }
    }
}