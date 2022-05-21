package es.uji.al394516.aviajar

import es.uji.al394516.aviajar.classes.Travel

class PresenterTE(val view: ITravelEdition, val model: Model) {
    fun travelExists(currentTravel: Travel?) {
        if (currentTravel == null)
            return

        //si tenemos viaje, rellenar los campos y bloquear edicion por el usuario
        view.fillLayout()
        view.canUserModifyTravel(false)
    }

}