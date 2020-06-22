package com.teyyihan.rickandmorty.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos")
data class CharacterModel(

    @SerializedName("created")
    var created: String?,
    @ColumnInfo(name="gender")
    @SerializedName("gender")
    var gender: String?,
    @PrimaryKey
    @SerializedName("id")
    var _id: Int?,
    @SerializedName("image")
    var _image: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("species")
    var species: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("url")
    var url: String?,
    var prevKey : Int?,
    var nextKey : Int?
)