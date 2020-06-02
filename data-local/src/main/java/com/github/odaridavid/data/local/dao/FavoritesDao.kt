/**
 *
 * Copyright 2020 David Odari
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *            http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
package com.github.odaridavid.data.local.dao

import androidx.room.*
import com.github.odaridavid.data.local.mappers.toEntity
import com.github.odaridavid.data.local.models.FavoriteEntity
import com.github.odaridavid.data.local.models.FavoriteWithFilms
import com.github.odaridavid.data.local.models.FilmEntity
import com.k0d4black.theforce.domain.models.Favorite

@Dao
interface FavoritesDao {

    @Query("DELETE FROM favorites")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM favorites WHERE id=:id")
    suspend fun delete(id: Int): Int

    @Transaction
    @Query("SELECT * FROM favorites WHERE id=:id")
    suspend fun getById(id: Int): FavoriteWithFilms

    @Transaction
    @Query("SELECT * FROM favorites WHERE name=:name")
    suspend fun getByName(name: String): FavoriteWithFilms

    @Transaction
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteWithFilms>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEntity: FavoriteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(filmEntity: FilmEntity): Long

    @Transaction
    suspend fun insert(favorite: Favorite) {
        val favId = insert(favorite.toEntity())
        for (film in favorite.films) {
            val filmEntity = film.toEntity(favId)
            insert(filmEntity)
        }
    }

}