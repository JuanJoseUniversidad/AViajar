package es.uji.al394516.aviajar

interface IMainView {
    fun toNextActivity()
    fun toTravelResults()
    fun enableButton(enable:Boolean)
    fun showMessage(message: String)
}