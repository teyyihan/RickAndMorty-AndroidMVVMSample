package com.teyyihan.rickandmorty.model


data class CharacterQueryModel(
    var name : String? = null,
    var status : CharacterLifeStatus? = null,
    var gender : CharacterGender? = null
)

enum class CharacterLifeStatus{
    ALIVE,
    DEAD,
    UNKNOWN
}

enum class CharacterGender{
    FEMALE,
    MALE,
    GENDERLESS,
    UNKNOWN
}

