package es.uji.al394516.aviajar

import android.view.View
import android.widget.LinearLayout
import es.uji.al394516.aviajar.classes.Expense
import es.uji.al394516.aviajar.classes.Person
import es.uji.al394516.aviajar.classes.Travel

class PresenterTE(val view: ITravelEdition, val model: Model) {

    var places:List<String> = listOf("Undefined")

    init{
        view.eeImageVisible = false

        model.clearPersonList()//To rebuild the list from sratch and avoid future problems
        model.clearExpensesList()

        getPlacesNetwork()
    }


    fun resetAllExpensesPerPerson(){
        model.resetMapAllExpense()
    }

    fun getPlacesNetwork(){
        model.getPlaces({
            view.showPlaces(it)
            places = it
            view.enableProgressBar(View.INVISIBLE);
            //view.activateActivity();
        },{
            view.showPlaces(places)
            view.showMessage("Could not connect to the database")
            view.enableProgressBar(View.INVISIBLE);
        })
    }

    fun travelExists(currentTravel: Travel?, editMode:Boolean) {
        //si no tenemos viaje, activar edicion de viaje
        if (currentTravel == null){
            view.canUserModifyTravel(true)
            return
        }else if(editMode == true){
            //si tenemos viaje, rellenar los campos y bloquear edicion por el usuario
            view.fillLayout()
            view.canUserModifyTravel(true)
        }else{
            //si tenemos viaje, rellenar los campos y bloquear edicion por el usuario
            view.fillLayout()
            view.canUserModifyTravel(false)
        }
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

    fun deletePerson(index: Int): Boolean{
        //mirar si no es la unica persona
        if (model.getAuxPeople().size <= 1)
            return false

        model.deletePerson(index)
        return true
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

    fun insertTravel(id:Int,name:String,place:String){
        if(name == ""){
            view.createAlertDialog(view.getResourceString(R.string.noAnyadido_str),view.getResourceString(R.string.debeTenerNombre_str))

        }else if(place==""){
            view.createAlertDialog(view.getResourceString(R.string.noAnyadido_str),view.getResourceString(R.string.debeTenerLugar_str))

        }else if(model.getAuxPeople().size < 1 || model.getAuxGasto().size < 1) {
            view.createAlertDialog(view.getResourceString(R.string.noAnyadido_str),view.getResourceString(R.string.debeTenerPersonaYGasto_str))

        }else{
            model.deleteTravel(Travel(id,name,place,model.getAuxPeople(),model.getAuxGasto()),{
                model.insertTravelDatabase(id, name, place)
                view.setTravel(Travel(id,name,place,model.getAuxPeople(),model.getAuxGasto()))
                view.createAlertDialog(view.getResourceString(R.string.viajeInsertado_str),view.getResourceString(R.string.viajeAgregadoBDD_str), view::toMainActivity)
            },{
                view.setTravel(Travel(id,name,place,model.getAuxPeople(),model.getAuxGasto()))
                //view.showMessage(it.toString())
                //view.createAlertDialog("Error","Ha sucedido un error inesperado", view::toMainActivity)
            })
        }
    }

    fun showDeleteTravel(){
        view.createAlertDialog(view.getResourceString(R.string.eliminarViaje_str),view.getResourceString(R.string.confirmarEliminacion_str), view::deleteTravel, true)
    }

    fun deleteTravel(travel:Travel?){
        model.deleteTravel(travel,{
            view.toMainActivity()
        },{
            view.showMessage(view.getResourceString(R.string.errorBorrando_str))
        })
    }

    fun setPeople(people:MutableList<Person>){
        model.setAuxPeople(people)
    }

    /**
     * Function that check if an Easter Egg condition occurs
     * If there is an EE, show and change the image, sleep 1 second and continues
     * Otherwise nothing
     */
    fun checkEE(travelName: String, placeName: String){
        var eeNecesaryConditions = false
        var image: Int? = null

        val people = model.getAuxPeople()
        val expenses = model.getAuxGasto()

        /**
         * Check if eeNecesaryConditions is false
         * Then check If there is EE:
         *  1º set eeNecesaryConditions to true
         *  2º set the "path" of the image (R.drawable...)
         */
        //region EasterEggs
        //EE1   Monchito001
        if (!eeNecesaryConditions){
            if (travelName == "25.07.20" && placeName == "Colombia" && people.count() == 2 && people[0].name == "Monchito" && people[1].name == "Señora" && expenses.count() == 1 && expenses[0].name == "Pulseras"){
                eeNecesaryConditions = true
                image = R.drawable.yellow_heart_90
            }
        }
        
        //EE2   Juanjo001
        if (!eeNecesaryConditions){
            if (travelName == "r/Place" && placeName == "France" && people.count() == 1 && expenses.count() == 1 && expenses[0].name == "Bots"){
                eeNecesaryConditions = true
                image = R.drawable.blank
            }
        }
        //EE...
        //else if(...)
        //EEn

        //endregion

        if (eeNecesaryConditions) {
            //Show and change image
            view.eeImageVisible = true
            view.setEEImage(image!!)

            //sleep 1 second
        }
    }
}
