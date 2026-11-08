package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val restaurantDao: RestaurantDao
) : ViewModel() {
    val restaurants = restaurantDao.getAllRestaurants().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // Suspend so the caller can wait for the database write before navigating.
    suspend fun addRestaurant(restaurant: NewRestaurant) {
        val existing = restaurant.id?.let { restaurantDao.getRestaurantById(it) }
            ?: restaurantDao.getRestaurantByName(restaurant.name.trim())
        val entity = RestaurantEntity(
            id = existing?.id ?: 0,
            name = restaurant.name.trim(),
            cuisines = restaurant.cuisines.joinToString(", "),
            rating = restaurant.rating,
            address = restaurant.address.trim(),
            phone = restaurant.phone.trim(),
            notes = restaurant.notes,
            photo = restaurant.photo,
            isFavorite = existing?.isFavorite ?: false
        )
        if (existing == null) restaurantDao.insertRestaurant(entity)
        else restaurantDao.updateRestaurant(entity)
    }

    fun deleteRestaurant(name: String) = viewModelScope.launch {
        restaurantDao.deleteRestaurantByName(name)
    }


    fun updateRating(name: String, rating: Int) = viewModelScope.launch {
        restaurantDao.updateRating(name, rating)
    }

}
