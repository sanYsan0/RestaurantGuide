
package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RestaurantViewModelFactory(
    private val restaurantDao: RestaurantDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(RestaurantViewModel::class.java)) {
            return RestaurantViewModel(restaurantDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
