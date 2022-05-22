package es.uji.al394516.aviajar

interface ITravelEdition {
    fun fillLayout()
    fun canUserModifyTravel(canEdit: Boolean)
    fun createAddPersonDialog()
}