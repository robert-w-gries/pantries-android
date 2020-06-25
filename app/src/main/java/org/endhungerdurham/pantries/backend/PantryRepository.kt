package org.endhungerdurham.pantries.backend

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.endhungerdurham.pantries.R
import retrofit2.Retrofit
import retrofit2.create

class PantryRepository {
    private val url = Resources.getSystem().getString(R.string.pantries_api_url)
    private val pantryService: PantryService = Retrofit.Builder().baseUrl(url).build().create<PantryService>(PantryService::class.java)
    fun getPantries(): LiveData<List<Pantry>> {
        val data = MutableLiveData<Pantry>()
    }
}