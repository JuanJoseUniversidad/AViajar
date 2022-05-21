package es.uji.al394516.aviajar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import es.uji.al394516.aviajar.classes.Travel

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var mirarViajesButton: Button

    private lateinit var presenter: PresenterMainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO("QUITAR ESTO CUANDO TERMINEMOS DE TESTEAR")
        /*val intent = Intent(this@MainActivity,TravelResultsActivity::class.java)
        intent.putExtra("Editar", true)

        startActivity(intent)*/

        mirarViajesButton = findViewById(R.id.mirarViajesButton)

        presenter = PresenterMainView(this, Model(applicationContext))

        //TODO("Mirar si la bdd está vacía -> bloquear botón mirarViajes")

    }

    fun onNuevoViaje(view: View) {
        presenter.onNuevoViaje()
    }

    fun onMirarViajes(view: View) {
        presenter.onMirarViajes()
    }

    //region IMainView functions
    override fun toNextActivity() {
        val intent = Intent(this@MainActivity, TravelEditionActivity::class.java)
        intent.putExtra("EditMode", true)
        intent.putExtra("CurrentTravel", null as Travel)

        startActivity(intent)
    }

    override fun toTravelResults() {
        val intent = Intent(this@MainActivity, TravelResultsActivity::class.java)
        intent.putExtra("EditMode", true)

        startActivity(intent)
    }
    //endregion
}