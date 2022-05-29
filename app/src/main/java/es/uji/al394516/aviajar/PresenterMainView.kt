package es.uji.al394516.aviajar

class PresenterMainView(val view: IMainView, model: Model) {

    init {
        model.getAllTravels({
                view.enableButton(true)
        },{
            view.showMessage(it.toString())
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