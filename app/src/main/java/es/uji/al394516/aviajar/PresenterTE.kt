package es.uji.al394516.aviajar

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

    fun editPerson(person: Person, index:Int){
        model.editPersonList(person,index)
    }
}