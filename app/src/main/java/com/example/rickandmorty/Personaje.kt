package com.example.rickandmorty

import java.io.Serializable

class Personaje (
    var id: Int,
    val name: String,
    var status: String,
    var specie: String,
    val type: String,
    var gender: String,
    var origin: Origin,
    var location: Location,
    var image: String,
    var episode: Any,
    var url: String,
    var created:String
    ): Serializable
{
}