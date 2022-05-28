package es.uji.al394516.aviajar.dialogs

import android.view.View
import android.widget.Button
import android.widget.TextView
import es.uji.al394516.aviajar.classes.Personid

interface IDialogsFunctions {
    fun onAccept(text:String, personLayout: View? = null, internalUse: Boolean)
    fun onOkExpense(name: String, totalPrice: Double, person_expense: MutableMap<Personid, Double>)
}