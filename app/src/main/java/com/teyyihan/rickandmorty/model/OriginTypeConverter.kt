package com.teyyihan.rickandmorty.model

import androidx.room.TypeConverter

class OriginTypeConverter {

    @TypeConverter
    fun stringToOrigin(data: String?): Origin? {
        return data?.substringAfter("%%%")?.let { Origin(data.substringBefore("%%%"), it) }
    }

    @TypeConverter
    fun originToString(someObjects: Origin?): String? {
        return someObjects?.name+"%%%"+someObjects?.url
    }

}