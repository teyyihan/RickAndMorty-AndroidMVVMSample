package com.teyyihan.rickandmorty.model

import androidx.room.TypeConverter

/**
 *  Can't store custom objects within a entity without a type converter.
 *  We need to store where character is from.
 */

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