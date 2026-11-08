package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val cuisines: String,
    val rating: Int,
    val address: String,
    val phone: String,
    val notes: String,
    val photo: String,
    val isFavorite: Boolean = false
)