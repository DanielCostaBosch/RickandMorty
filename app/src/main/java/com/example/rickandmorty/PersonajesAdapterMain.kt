package com.example.rickandmorty


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.CardRecyclerMainBinding


class PersonajesAdapterMain (private val personajes: ArrayList<String>): RecyclerView.Adapter<PersonajesAdapterMain.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_recycler_main, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val personaje = personajes[position]
        /*
                with(holder) {
                    binding.tvNamecharacterCard.setText(personaje.name)
                    binding.ivCard.setImageURI()
                }

         */
        //viewHolder.textView.text = personajes[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = personajes.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CardRecyclerMainBinding.bind(view)
    }
}