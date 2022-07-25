package es.uji.al394516.aviajar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import es.uji.al394516.aviajar.classes.Travel

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var mirarViajesButton: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var presenter: PresenterMainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mirarViajesButton = findViewById(R.id.mirarViajesButton)
        progressBar= findViewById(R.id.progressBarMain)

        presenter = PresenterMainView(this, Model(applicationContext))

    }

    fun onNuevoViaje(view: View) {
        presenter.onNuevoViaje()
    }

    fun onMirarViajes(view: View) {
        presenter.onMirarViajes()
    }

    //region IMainView functions

    override fun enableButton(enable:Boolean){
        mirarViajesButton.setEnabled(enable)
        progressBar.visibility = View.INVISIBLE
    }

    /**
     * Shows an message via Toast
     * @param message An String of the message
     */
    override fun showMessage(message: String){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast.show()
    }

    override fun toNextActivity() {
        val intent = Intent(this@MainActivity, TravelEditionActivity::class.java)
        intent.putExtra("EditMode", true)
        intent.putExtra("CurrentTravel", null as Travel?)

        startActivity(intent)
    }

    override fun toTravelResults() {
        val intent = Intent(this@MainActivity, TravelResultsActivity::class.java)
        intent.putExtra("EditMode", true)

        startActivity(intent)
    }
    //endregion
}