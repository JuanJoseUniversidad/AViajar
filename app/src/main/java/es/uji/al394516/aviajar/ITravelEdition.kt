package es.uji.al394516.aviajar

import android.view.View
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Travel

interface ITravelEdition {
    var precioTotal: String

    fun fillLayout()
    fun canUserModifyTravel(canEdit: Boolean)
    fun createAddPersonDialog(title:String, personLayout: View? = null)
    fun createAddGastoDialog(title: String, gastoLayout: View? = null, gasto: Expense?)
    fun createAlertDialog(title:String, text:String, function: (() -> Unit)? = null, cancelButton:Boolean = false)
    fun <T>createConfirmationDialog(title: String, text: String, parameter: List<T>? = listOf(), function: ((personLayout:List<T>?) -> Unit)? = null)
    fun showPlaces(ingredients: List<String>)
    fun showMessage(message: String)
    fun enableProgressBar(enable: Int)
    fun setTravel(travel :Travel)
    fun getTravel(): Travel?
    fun toEditMode()
    fun deleteTravel()
    fun toMainActivity()
}