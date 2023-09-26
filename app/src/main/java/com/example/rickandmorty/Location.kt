package com.example.rickandmorty

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Location(
    var name :String?,
    var url: String?
){
}