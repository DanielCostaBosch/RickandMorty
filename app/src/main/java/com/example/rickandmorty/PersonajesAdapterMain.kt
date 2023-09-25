package com.example.rickandmorty


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.CardRecyclerMainBinding
import java.util.concurrent.Executors


class PersonajesAdapterMain (private val personajes: ArrayList<Personaje>): RecyclerView.Adapter<PersonajesAdapterMain.ViewHolder>() {

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

        with(holder) {
            binding.tvNamecharacterCard.text = personaje.name
            binding.tvOriginValue.text = personaje.origin.name
            binding.tvGenderValue.text = personaje.gender
            binding.tvSpeciesValue.text = personaje.specie
            binding.tvTypeValue.text = personaje.type
            binding.tvStatusValue.text = personaje.status

            val executor = Executors.newSingleThreadExecutor()
            val handler = android.os.Handler(Looper.getMainLooper())
            var imageBm : Bitmap? = null
            executor.execute {
                try {
                    val `in` = java.net.URL(personaje.image).openStream()
                    imageBm = BitmapFactory.decodeStream(`in`)
                    handler.post {
                        binding.ivCard.setImageBitmap(imageBm)
                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = personajes.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CardRecyclerMainBinding.bind(view)
    }
}