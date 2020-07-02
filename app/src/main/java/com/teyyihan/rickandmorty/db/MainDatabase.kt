/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teyyihan.rickandmorty.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teyyihan.rickandmorty.Consts
import com.teyyihan.rickandmorty.model.CharacterModel
import com.teyyihan.rickandmorty.model.OriginTypeConverter

@Database(
        entities = [CharacterModel::class],
        version = 4,
        exportSchema = false
)
@TypeConverters(OriginTypeConverter::class)
abstract class MainDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharacterDao


    /**
     *  Will be called from ViewmodelModule DI
     */
    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        MainDatabase::class.java, Consts.DATABASE_NAME)
                        .build()
    }
}
