package com.teyyihan.rickandmorty

import android.app.Application
import com.teyyihan.rickandmorty.db.PreferencesRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application()