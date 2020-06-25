package com.teyyihan.rickandmorty.db

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teyyihan.rickandmorty.Consts
import com.teyyihan.rickandmorty.Consts.PREFERENCE_NIGHT_MODE
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    val sharedPreferences: SharedPreferences
) {

    val nightMode : Int
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, Consts.PREFERENCE_NIGHT_MODE_DEF_VAL)

    val nightModeLive : MutableLiveData<Int> = MutableLiveData()

    var isDarkTheme : Boolean = false
        get() = nightMode == AppCompatDelegate.MODE_NIGHT_YES
        set(value) {
            sharedPreferences.edit().putInt(PREFERENCE_NIGHT_MODE, if (value) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }).apply()
            field = value
        }

    val isDarkThemeLive: MutableLiveData<Boolean> = MutableLiveData()

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFERENCE_NIGHT_MODE -> {
                    nightModeLive.value = nightMode
                    isDarkThemeLive.value = isDarkTheme
                }
            }
        }

    init {
        // Init preference LiveData objects.
        nightModeLive.value = nightMode
        isDarkThemeLive.value = isDarkTheme

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }


}