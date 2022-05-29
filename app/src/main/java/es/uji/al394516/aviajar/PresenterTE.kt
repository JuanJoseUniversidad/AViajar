package es.uji.al394516.aviajar

import android.view.View
import android.widget.LinearLayout
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Travel

class PresenterTE(val view: ITravelEdition, val model: Model) {

    init{
        model.clearPersonList()//To rebuild the list from sratch and avoid future problems
    }

    fun travelExists(currentTravel: Travel?) {
        //si no tenemos viaje, activar edicion de viaje
        if (currentTravel == null){
            view.canUserModifyTravel(true)
            return
        }

        //si tenemos viaje, rellenar los campos y bloquear edicion por el usuario
        view.fillLayout()
        view.canUserModifyTravel(false)
    }

    fun addNewPerson(person:Person){
        model.addPersonList(person)
    }

    /**
     * Function that receives [newExpense] and pass it to the model to add it to an auxiliary list
     */
    fun addNewExpense(newExpense: Expense) {
        model.addExpenseList(newExpense)
    }

    fun editPerson(person: Person, index:Int){
        model.editPersonList(person,index)
    }

    /**
    * Function that receives [newExpense] and its [index] in the model to pass them to the model to modify it in the auxiliary list
     * @param newExpense
     * @param index
    */
    fun editGasto(newExpense: Expense, index: Int) {
        model.editGastoList(newExpense, index)
    }

    fun debugPersonList(){
        model.debugPersons()
    }

    fun deletePerson(index: Int){
        model.deletePerson(index)
    }

    fun deleteGasto(gastoLayout: View, linearLayout: LinearLayout) {
        //borrar gasto de la lista
        val index = linearLayout.indexOfChild(gastoLayout)
        model.deleteGasto(index)

        //actualizar scroll
        linearLayout.removeView(gastoLayout)
    }

    fun getPerson(index:Int):Person{
        return model.getPersonList(index)
    }

    /**
     * Sets the text of the PrecioTotal TextView
     */
    fun setPrecioTotal() {
        val precioTotal: Double = model.precioTotal()

        view.precioTotal = precioTotal.toString() + "€"
    }
}