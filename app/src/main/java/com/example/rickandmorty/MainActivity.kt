package com.example.rickandmorty


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import com.example.rickandmorty.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var listPersonajes = ArrayList<Personaje>()
    private lateinit var personajesHomeAdapter: PersonajesAdapterMain
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (!isConnected) {
            Log.d("entraaa", "sii")
            binding.tvNoInternet.text = "Comprueba la conexión a internet."
            binding.tvNoInternet.visibility = View.VISIBLE
            binding.tvNoInternet.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.black
                )
            )
        }

        getPersonajes()


    }

    private fun getPersonajes() {

        val url =
            "https://rickandmortyapi.com/api/character"


        val jsonArrayRequest = JsonArrayRequest(url,
            { response ->
                var jsonObject: JSONObject? = null
                for (i in 0 until response.length()) {
                    try {
                        jsonObject = response.getJSONObject(i)

                        Log.d("vaaaa",jsonObject.toString())
                        /* var evento = Evento(
                             jsonObject.getString("id"),
                             jsonObject.getString("titulo"),
                             jsonObject.getString("id_creador"),
                             jsonObject.getString("ubicacion"),
                             jsonObject.getString("fecha"),
                             jsonObject.getString("hora"),
                             jsonObject.getString("descripcion"),
                             jsonObject.getString("aforoMax").toInt(),
                             jsonObject.getString("participantes"),
                             jsonObject.getString("imagen"),
                             jsonObject.getString("latitud"),
                             jsonObject.getString("longitud"),
                             null
                         )*/

                        /* listPersonajes.add(jsonObject.)
                         personajesHomeAdapter = PersonajesAdapterMain(listPersonajes)
                         linearLayoutManager = LinearLayoutManager(this)
                         binding.recyclerviewMain.apply {
                             layoutManager = linearLayoutManager
                             adapter = personajesHomeAdapter
                         }*/
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            }) { }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonArrayRequest)
    }
}