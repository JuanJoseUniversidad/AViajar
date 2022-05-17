package es.uji.al394516.aviajar

class PresenterMainView(val view: IMainView, model: Model) {
    fun onNuevoViaje() {
        view.toNextActivity()
    }

    fun onMirarViajes() {
        view.toTravelResults()
    }
}