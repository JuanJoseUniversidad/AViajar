package es.uji.al394516.aviajar

import android.view.View

interface ITravelEdition {
    fun fillLayout()
    fun canUserModifyTravel(canEdit: Boolean)
    fun createAddPersonDialog(title:String, personLayout: View? = null)
    fun createAlertDialog(title:String, text:String)
    fun <T>createConfirmationDialog(title: String, text: String, parameter: List<T>? = listOf(), function: ((personLayout:List<T>?) -> Unit)? = null)
    fun showPlaces(ingredients: List<String>)
    fun showMessage(message: String)
}