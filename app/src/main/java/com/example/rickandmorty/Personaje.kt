package com.example.rickandmorty

import java.io.Serializable

data class Personaje (
    var id: Int,
    var name: String,
    var status: String,
    var specie: String,
    var type: String,
    var gender: String,
    var origin: Origin,
    var location: Location,
    var image: String,
    var episode: ArrayList<String>,
    var url: String,
    var created:String
    ) : Serializable {

}