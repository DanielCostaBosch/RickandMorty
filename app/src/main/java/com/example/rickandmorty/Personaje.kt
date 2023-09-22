package com.example.rickandmorty

import java.io.Serializable

data class Personaje(
    val name: String,
    var gender: String,
    val type: String,
    var specie: String,
    var status: String,
    var origin: String,
    var episode: String,
    var currentLocation: String,
    var image: String
): Serializable