package es.uji.al394516.aviajar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.uji.al394516.aviajar.classes.Travel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this@MainActivity,TravelResultsActivity::class.java)
        intent.putExtra("Editar", true)

        startActivity(intent)
    }
}