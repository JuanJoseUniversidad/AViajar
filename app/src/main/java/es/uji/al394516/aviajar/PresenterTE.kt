package es.uji.al394516.aviajar

import es.uji.al394516.aviajar.classes.Travel

class PresenterTE(val view: ITravelEdition, val model: Model) {
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

}