package es.uji.al394516.aviajar

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import es.uji.al394516.aviajar.database.SingletonHolder
import org.json.JSONException
import org.json.JSONObject

class Network private constructor(context: Context){
    companion object: SingletonHolder<Network, Context>(::Network)

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    /**
     * Funcion which returns the places given by the API
     */
    fun getPlaces(listener: Response.Listener<List<String>>, errorListener: Response.ErrorListener){
        val url = "https://laravel-world.com/api/countries"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                parsePlaces(response, listener, errorListener)
            },
            errorListener
        )
        requestQueue.add(jsonObjectRequest)
    }

    /**
     * Function called Juanjo el Hermoso and Mon El Precioso, from getPlaces
     * Gets the list of places which belongs to JSONObject
     */
    private fun parsePlaces(response: JSONObject, listener: Response.Listener<List<String>>, errorListener: Response.ErrorListener) {
        val places = ArrayList<String>()
        try {
            val placeArray = response.getJSONArray("data")
            for (i in 0 until placeArray.length()) {
                val placeObject = placeArray[i] as JSONObject
                val name = placeObject.getString("name")
                places.add(name)
            }
        } catch (e: JSONException) {
            errorListener.onErrorResponse(VolleyError("BAD JSON FORMAT"))
            return
        }
        // Sort the result by name
        places.sortBy { it }
        // Return the result using the listener
        listener.onResponse(places)
    }
}