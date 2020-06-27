package com.teyyihan.rickandmorty.model


import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "characters")
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
    var nextKey : Int?,
    var filteredPrevKey : Int?,
    var filteredNextKey : Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterModel> {
        override fun createFromParcel(parcel: Parcel): CharacterModel {
            return CharacterModel(parcel)
        }

        override fun newArray(size: Int): Array<CharacterModel?> {
            return arrayOfNulls(size)
        }
    }
}