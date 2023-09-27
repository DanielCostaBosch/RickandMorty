package com.example.rickandmorty

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.rickandmorty.databinding.ActivityDetallePersonajeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.concurrent.Executors

class DetallePersonaje : AppCompatActivity() {
    private lateinit var binding: ActivityDetallePersonajeBinding
    private lateinit var episodiosAdapter: EpisodiosAdapterDetalle
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var context: Context
    private var mRequestQueue: RequestQueue? = null
    private var mRequestQueueEpiCharDet: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePersonajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this


        if (intent.extras?.getInt("id") != null) {
           var idPersonaje = intent.extras?.getInt("id")
            getCharDetails(idPersonaje!!)
        }

        binding.btnBackDet.setOnClickListener {
            finish()
        }
    }
    fun getCharDetails(id: Int) {
        var url = "https://rickandmortyapi.com/api/character/$id";
        CoroutineScope(Dispatchers.IO).launch {
            // Request
            mRequestQueue = Volley.newRequestQueue(context)
            // Json Request inicializado
            var request = JsonObjectRequest(Request.Method.GET, url, null, { resultPers ->
                // Recogemos lo que nos interesa (el results)
                // donde estan los datos del personaje

                try {
                    var name = resultPers.getString("name")
                    var status = resultPers.getString("status")
                    var specie = resultPers.getString("species")
                    var type = resultPers.getString("type")
                    if (type.equals("")) {
                        type = "unknown"
                    }
                    var gender = resultPers.getString("gender")
                    var originJson = resultPers.getJSONObject("origin")
                    var origin =
                        Origin(originJson.getString("name"), originJson.getString("url"))
                    var locationJson = resultPers.getJSONObject("location")
                    var location =
                        Location(locationJson.getString("name"), locationJson.getString("url"))
                    var image = resultPers.getString("image")
                    var arrEpisodes = resultPers.getJSONArray("episode")
                    var episode = ArrayList<String>()
                    for (i in 0 until arrEpisodes.length()) {
                        episode.add(arrEpisodes.getString(i))
                    }
                    var urlCharacter = resultPers.getString("url")
                    var created = resultPers.getString("created")

                    var personaje = Personaje(
                        id,
                        name,
                        status,
                        specie,
                        type,
                        gender,
                        origin,
                        location,
                        image,
                        episode,
                        urlCharacter,
                        created
                    )
                    anadir_valores(personaje)
                    anadir_recyclerEpi(personaje.episode)
                    } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
                Toast.makeText(context, "No hay coincidencias Morty!!", Toast.LENGTH_LONG).show()
            })
            mRequestQueue?.add(request)
        }
    }
    // Funcion para dar valores a la vista
    fun anadir_valores (personaje: Personaje) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = android.os.Handler(Looper.getMainLooper())
        var imageBm : Bitmap? = null
        // corrutina para la imagen
        executor.execute {
            try {
                val `in` = java.net.URL(personaje.image).openStream()
                imageBm = BitmapFactory.decodeStream(`in`)
                handler.post {
                    binding.ivPersonDet.setImageBitmap(imageBm)
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
            binding.tvNameDet.gravity = Gravity.CENTER_VERTICAL
            binding.tvNameDet.text = personaje.name
            binding.tvValueLocationDet.text = personaje.location.name
            binding.tvValueTypeDet.text = personaje.type
            binding.tvValueSpecieDet.text = personaje.specie
            binding.tvValueGenderDet.text = personaje.gender
            binding.tvValueOriginDet.text = personaje.origin.name
            binding.tvValueStatusDet.text = personaje.status

            // Depende del estado un outline
            when(personaje.status) {
                "Alive" -> binding.containerIvPersonDet.setBackgroundResource(R.drawable.outline_det_alive)
                "Dead" -> binding.containerIvPersonDet.setBackgroundResource(R.drawable.outline_det_dead)
                "unknown" -> binding.containerIvPersonDet.setBackgroundResource(R.drawable.outline_det_unknown)
            }

        }
    }

    /*
        Funcion para alimentar el recylerview de los episodios
        en los que aparece el personaje
     */
    fun anadir_recyclerEpi(arrEpi:ArrayList<String>){
        // Array recoge personajes del episodio
        var arrListNameEpi = ArrayList <String> ()
        // Corrutina para funcionar en segundo plano
        CoroutineScope(Dispatchers.IO).launch {

            // Request episodios
            mRequestQueueEpiCharDet = Volley.newRequestQueue(context)
            // Request cada url
            for (i in 0 until arrEpi.size) {
                // Json Request inicializado
                var requestPers = JsonObjectRequest(Request.Method.GET, arrEpi[i], null, { resultPers ->
                    try {
                        // nombre episodio
                        var nameEpi = resultPers.getString("name")
                        arrListNameEpi.add(nameEpi)
                    } catch (e:JSONException){
                        e.printStackTrace()
                    }
                    // si la lista no esta vacía iniciamos recycler
                    if(arrListNameEpi.size > 0) {
                        episodiosAdapter = EpisodiosAdapterDetalle(arrListNameEpi)
                        linearLayoutManager = LinearLayoutManager(context)
                        binding.recyclerEpisodeDet.apply {
                            layoutManager = linearLayoutManager
                            adapter = episodiosAdapter
                        }
                    }
                }, {
                    Toast.makeText(context, "No hay episodios Morty!!",Toast.LENGTH_LONG).show()
                })
                mRequestQueueEpiCharDet?.add(requestPers)
            }
        }
    }
}