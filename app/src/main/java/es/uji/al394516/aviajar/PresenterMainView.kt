package es.uji.al394516.aviajar

class PresenterMainView(val view: IMainView, model: Model) {

    init {
        model.getAllTravels({
                view.enableButton(true)
        },{
            view.showMessage("No existe ningun viaje en la base de datos")
            view.enableButton(false)
        })
    }

    fun onNuevoViaje() {
        view.toNextActivity()
    }

    fun onMirarViajes() {
        view.toTravelResults()
    }
}