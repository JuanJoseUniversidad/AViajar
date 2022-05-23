package es.uji.al394516.aviajar.dialogs

import android.view.View
import android.widget.Button
import android.widget.TextView

interface IDialogsFunctions {
    fun onAccept(text:String, personLayout: View? = null)
}