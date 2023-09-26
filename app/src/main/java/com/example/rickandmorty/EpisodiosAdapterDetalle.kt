package com.example.rickandmorty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.CardRecyclerEpisodiosBinding
import com.example.rickandmorty.databinding.CardRecyclerMainBinding

class EpisodiosAdapterDetalle(private val episodios: ArrayList<String>): RecyclerView.Adapter<EpisodiosAdapterDetalle.ViewHolder>() {
    // Creamos vista layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_recycler_episodios, viewGroup, false)
        return ViewHolder(view)
    }

    // Damos valores a los distintos items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var episodio = episodios[position]
        with(holder) {
            binding.tvNameEpiDetRec.text = episodio
        }
    }

    // Numero de items
    override fun getItemCount() = episodios.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CardRecyclerEpisodiosBinding.bind(view)
    }
}