package es.uji.al394516.aviajar.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import es.uji.al394516.aviajar.R
import java.lang.ClassCastException

class AddPersonDialog : DialogFragment() {
    private lateinit var personListener: IDialogsFunctions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        personListener = try {
            context as IDialogsFunctions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context does not listen to ratings")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null)
            throw IllegalStateException("Activity cannot be null")

        val aux = requireActivity()     //como no es null la activity pues esto tiene valor seguro

        var text: EditText
        val view = aux.layoutInflater.inflate(R.layout.edittext_dialog, null)
        with(view){
            text = findViewById(R.id.editTextPersonName)

        }

        return AlertDialog.Builder(aux).run {
            setTitle("Add person")
            setView(view)
            setPositiveButton("OK") { dialog, which ->
                if(text.text.toString() != ""){
                    personListener.onAccept(text.text.toString())
                }else{
                    text.setHintTextColor(Color.RED)
                }
            }
            setNegativeButton("CANCEL") { dialog, which -> dialog.cancel() }
            create()
        }
    }
}