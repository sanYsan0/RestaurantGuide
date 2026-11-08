
package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Insert
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Delete
    suspend fun deleteRestaurant(restaurant: RestaurantEntity)

    @Query("SELECT * FROM restaurants ORDER BY name ASC")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE name = :name LIMIT 1")
    suspend fun getRestaurantByName(name: String): RestaurantEntity?

    @Query("DELETE FROM restaurants WHERE name = :name")
    suspend fun deleteRestaurantByName(name: String)

    @Query("UPDATE restaurants SET rating = :rating WHERE name = :name")
    suspend fun updateRating(name: String, rating: Int)

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): RestaurantEntity?
}
