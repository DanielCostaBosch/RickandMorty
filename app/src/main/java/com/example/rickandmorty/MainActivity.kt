package com.example.rickandmorty


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.rickandmorty.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var personajesHomeAdapter: PersonajesAdapterMain
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var context: Context
    private var url = "https://rickandmortyapi.com/api/";
    private var mRequestQueue: RequestQueue? = null
    private var mRequestQueueEpiChar: RequestQueue? = null
    private var numPage = 1
    private var numEpisode = 1
    private var isPageSearch = false
    private var isEpisodeSearch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var extensionSearch = ""
        context = this

        // Vemos si hay conexion a intrnet
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        // Si no hay lo notificamos
        if (!isConnected) {
            binding.tvNoInternet.text = getString(R.string.fallo_conex_inter)
            binding.tvNoInternet.visibility = View.VISIBLE
            binding.tvNoInternet.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.black
                )
            )
        }

        // Por defcto siempre si podrá avanzar a la siguiente pag hasta el final
        // pero no retrodecedir si no avanzamos
        binding.nextButton.setBackgroundResource(R.color.main_green)
        binding.prevButton.isClickable = false

        // ACCION NEXT BUTTON, PASA A LA SIGUIENTE PAGINA O EPISODIO
        binding.nextButton.setOnClickListener {
            binding.prevButton.isClickable = true
            binding.prevButton.setBackgroundResource(R.color.main_green)
            if (isPageSearch) {
                if (numPage <= 41) {
                    binding.nextButton.setBackgroundResource(R.color.main_green)
                    numPage++
                    var urlextension = "character/?page=$numPage"
                    getCharacters(urlextension)
                }else {
                    binding.nextButton.setBackgroundResource(R.color.main_dark_grey)
                    binding.nextButton.isClickable = false
                }
            }
            if (isEpisodeSearch) {
                if (numEpisode <= 40) {
                    binding.nextButton.setBackgroundResource(R.color.main_green)
                    numEpisode++
                    var urlextension = "episode/$numEpisode"
                    getEpisodeCharacters(urlextension)
                }else {
                    binding.nextButton.setBackgroundResource(R.color.main_dark_grey)
                    binding.nextButton.isClickable = false
                }
            }
        }

        // ACCION PREV BUTTON, PASA A PAGINA O EPISODIO ANTERIOR
        binding.nextButton.setOnClickListener {
            binding.prevButton.isClickable = true
            binding.prevButton.setBackgroundResource(R.color.main_green)
            if (isPageSearch) {
                if (numPage > 1) {
                    binding.nextButton.setBackgroundResource(R.color.main_green)
                    numPage--
                    var urlextension = "character/?page=$numPage"
                    getCharacters(urlextension)
                }else {
                    binding.nextButton.setBackgroundResource(R.color.main_dark_grey)
                    binding.nextButton.isClickable = false
                }
            }
            if (isEpisodeSearch) {
                if (numEpisode > 1) {
                    binding.nextButton.setBackgroundResource(R.color.main_green)
                    numEpisode--
                    var urlextension = "episode/$numEpisode"
                    getEpisodeCharacters(urlextension)
                }else {
                    binding.nextButton.setBackgroundResource(R.color.main_dark_grey)
                    binding.nextButton.isClickable = false
                }
            }
        }

        // BUSQUEDA PERSONAJE
        binding.searchCharacter.setOnClickListener {
            binding.tvNameEpiPers.visibility = View.GONE
            isPageSearch = false
            isEpisodeSearch = false
            binding.etSearch.inputType = InputType.TYPE_CLASS_TEXT
            binding.containerSearchName.visibility = View.VISIBLE
            binding.nextButton.visibility = View.GONE
            binding.prevButton.visibility = View.GONE
            binding.etSearch.setHint(R.string.introduzca_nombre)
            binding.searchCharacter.setBackgroundResource(R.drawable.bar_bottom_background)
            binding.searchEpisode.setBackgroundResource(R.color.main_dark_grey)
            binding.searchPage.setBackgroundResource(R.color.main_dark_grey)
            binding.homeButton.setBackgroundResource(R.color.main_dark_grey)
            extensionSearch="character/?name="
        }

        // BUSQUEDA POR EPISODIO
        binding.searchEpisode.setOnClickListener {
            binding.tvNameEpiPers.visibility = View.VISIBLE
            binding.tvNameEpiPers.text = getString(R.string.nombre_del_episodio)
            isPageSearch = false
            isEpisodeSearch = true
            binding.etSearch.inputType = InputType.TYPE_CLASS_NUMBER
            binding.containerSearchName.visibility = View.VISIBLE
            binding.etSearch.setHint(R.string.introduzca_pagina)
            binding.searchEpisode.setBackgroundResource(R.drawable.bar_bottom_background)
            binding.searchPage.setBackgroundResource(R.color.main_dark_grey)
            binding.searchCharacter.setBackgroundResource(R.color.main_dark_grey)
            binding.homeButton.setBackgroundResource(R.color.main_dark_grey)
            extensionSearch="episode/"
        }


        // BUSQUEDA POR PÁGINA (BOTON LISTENER)
        binding.searchPage.setOnClickListener {
            binding.tvNameEpiPers.visibility = View.GONE
            isPageSearch = true
            isPageSearch = false
            binding.etSearch.inputType = InputType.TYPE_CLASS_NUMBER
            binding.containerSearchName.visibility = View.VISIBLE
            binding.etSearch.setHint(R.string.introduzca_pagina)
            binding.searchPage.setBackgroundResource(R.drawable.bar_bottom_background)
            binding.searchCharacter.setBackgroundResource(R.color.main_dark_grey)
            binding.searchEpisode.setBackgroundResource(R.color.main_dark_grey)
            binding.homeButton.setBackgroundResource(R.color.main_dark_grey)
            extensionSearch="character/?page="
        }

        // BOTON HOME, RCYCLERVIEW BASE (PAGE 1)
        binding.homeButton.setOnClickListener {
            isPageSearch = false
            isEpisodeSearch = false
            numPage = 1
            numEpisode = 1
            binding.containerSearchName.visibility = View.GONE
            binding.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.VISIBLE
            binding.prevButton.setBackgroundResource(R.color.main_dark_grey)
            binding.searchCharacter.setBackgroundResource(R.color.main_dark_grey)
            binding.searchEpisode.setBackgroundResource(R.color.main_dark_grey)
            binding.homeButton.setBackgroundResource(R.drawable.bar_bottom_background)
            var extensionUrl="character/?page=$numPage"
            getCharacters(extensionUrl)
        }

        // BOTON REALIZAR BUSQUEDA
        binding.searchButton.setOnClickListener {
            if (binding.etSearch.text.isEmpty()) {
                Toast.makeText(this, "Introduzca búsqueda", Toast.LENGTH_LONG).show()
            } else {
                // si buscamos por pagina, el rango es entre 1-42
                if (isPageSearch) {
                  regularBusquedaPag()
                }

                // si buscamos por episodio, el rango es entre 1-41
                if (isEpisodeSearch) {
                    regularBusquedaEpisodio()
                    var extraSearch = binding.etSearch.text
                    var searchUrl = extensionSearch + extraSearch
                    getEpisodeCharacters(searchUrl)
                } else {
                    var extraSearch = binding.etSearch.text
                    var searchUrl = extensionSearch + extraSearch
                    getCharacters(searchUrl)
                }
            }
        }

        // LLAMADA INICIAL RECOGE PERSONAJES DE LA PRIMERA PAGINA
        getCharacters("character/?page=$numPage")
    }

    /*
        Recoge los personajes de la URL con sus datos
        Realiza la llamada al recyclerview con los datos recogidos
     */
    private fun getCharacters(extensionUrl: String) {
        // Inicializamos array
        var arrPersonajes: ArrayList<Personaje> = ArrayList<Personaje>()
        // Url de Api
        var urlApi = url + extensionUrl
        // Corrutina para funcionar en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            // Request
            mRequestQueue = Volley.newRequestQueue(context)
            // Json Request inicializado
            var request = JsonObjectRequest(Request.Method.GET, urlApi, null, { result ->
                // Recogemos lo que nos interesa (el results)
                // donde estan los datos del personaje

                var jsonArrayRes = result.getJSONArray("results")
                for (i in 0 until jsonArrayRes.length()) {
                    try {
                        val jsonObj = jsonArrayRes.getJSONObject(i)
                        var id = jsonObj.getInt("id")
                        var name = jsonObj.getString("name")
                        var status = jsonObj.getString("status")
                        var specie = jsonObj.getString("species")
                        var type = jsonObj.getString("type")
                        if (type.equals("")) {
                            type = "Unknown"
                        }
                        var gender = jsonObj.getString("gender")
                        var originJson = jsonObj.getJSONObject("origin")
                        var origin =
                            Origin(originJson.getString("name"), originJson.getString("url"))
                        var locationJson = jsonObj.getJSONObject("location")
                        var location =
                            Location(locationJson.getString("name"), locationJson.getString("url"))
                        var image = jsonObj.getString("image")
                        var episode = jsonObj.get("episode")
                        var urlCharacter = jsonObj.getString("url")
                        var created = jsonObj.getString("created")

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
                        arrPersonajes.add(personaje)
                    } catch (e:JSONException){
                        e.printStackTrace()
                    }
                }
                // si la lista no esta vacía iniciamos recycler
                if(arrPersonajes.size > 0) {
                    personajesHomeAdapter = PersonajesAdapterMain(arrPersonajes)
                    linearLayoutManager = LinearLayoutManager(context)
                    binding.recyclerviewMain.apply {
                        layoutManager = linearLayoutManager
                        adapter = personajesHomeAdapter
                    }
                }
            }, {
                Toast.makeText(context, "No hay coincidencias Morty!!",Toast.LENGTH_LONG).show()
            })
            mRequestQueue?.add(request)
        }
    }



    // Recogemos episodio y personajes de dicho episodio
    private fun getEpisodeCharacters(extensionUrl: String) {
        // Url de Api
        var urlApi = url + extensionUrl
        var arrUrlPersonajes = ArrayList <String> ()
        // Array recoge personajes del episodio
        var arrCharactersEpi = ArrayList <Personaje> ()
        // Corrutina para funcionar en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            // Request
            mRequestQueue = Volley.newRequestQueue(context)
            // Json Request inicializado
            var request = JsonObjectRequest(Request.Method.GET, urlApi, null, { result ->
                // Recogemos lo que nos interesa
                // donde estan los datos del personaje
                try {
                    binding.tvNameEpiPers.text = result.getString("name")
                    var jsonArrPers = result.getJSONArray("characters")
                    for (i in 0 until jsonArrPers.length()) {
                        arrUrlPersonajes.add(jsonArrPers[i].toString())
                    }
                } catch ( e: JSONException) {
                    e.printStackTrace()
                }

                // recogemos los personajes
                // Request personajes
                mRequestQueueEpiChar = Volley.newRequestQueue(context)
                // Request cada url
                for (i in 0 until arrUrlPersonajes.size) {
                    // Json Request inicializado
                    var requestPers = JsonObjectRequest(Request.Method.GET, arrUrlPersonajes[i], null, { resultPers ->
                        try {
                            var id = resultPers.getInt("id")
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
                            var episode = resultPers.get("episode")
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
                            arrCharactersEpi.add(personaje)
                        } catch (e:JSONException){
                            e.printStackTrace()
                        }
                        // si la lista no esta vacía iniciamos recycler
                        if(arrCharactersEpi.size > 0) {
                            personajesHomeAdapter = PersonajesAdapterMain(arrCharactersEpi)
                            linearLayoutManager = LinearLayoutManager(context)
                            binding.recyclerviewMain.apply {
                                layoutManager = linearLayoutManager
                                adapter = personajesHomeAdapter
                            }
                        }
                    }, {
                        Toast.makeText(context, "No hay nadie Morty!!",Toast.LENGTH_LONG).show()
                    })
                    mRequestQueueEpiChar?.add(requestPers)
                }

            }, {
                Toast.makeText(context, "Error episodio",Toast.LENGTH_LONG).show()
            })
            mRequestQueue?.add(request)
        }
    }

    /*
        CONTROLAMOS LOS DATOS INTRODUCIDOS PARA BUSQUEDA EPISODIO
     */
    private fun regularBusquedaEpisodio(){
        binding.nextButton.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
        numEpisode = binding.etSearch.text.toString().toInt()
        if (numEpisode <= 1) {
            binding.nextButton.setBackgroundResource(R.color.main_green)
            numEpisode = 1
        } else if (numEpisode > 41) {
            binding.nextButton.setBackgroundResource(R.color.main_dark_grey)
            binding.prevButton.setBackgroundResource(R.color.main_green)
            binding.prevButton.isClickable = true
            numEpisode = 41
        } else {
            binding.nextButton.setBackgroundResource(R.color.main_green)
            binding.prevButton.setBackgroundResource(R.color.main_green)
        }
        binding.etSearch.setText(numEpisode.toString())
    }


    /*
        CONTROLAMOS LOS DATOS INTRODUCIDOS PARA BUSQUEDA PAGINA
     */
    private fun regularBusquedaPag(){
        binding.nextButton.visibility = View.VISIBLE
        binding.nextButton.visibility = View.VISIBLE
        numPage = binding.etSearch.text.toString().toInt()
        if (numPage <= 1) {
            binding.nextButton.setBackgroundResource(R.color.main_green)
            numPage = 1
        } else if (numPage > 42) {
            binding.nextButton.setBackgroundResource(R.color.main_dark_grey)
            binding.prevButton.setBackgroundResource(R.color.main_green)
            binding.prevButton.isClickable = true
            numPage = 42
        } else {
            binding.nextButton.setBackgroundResource(R.color.main_green)
            binding.prevButton.setBackgroundResource(R.color.main_green)
        }

        binding.etSearch.setText(numPage.toString())
    }
}