package es.uji.al394516.aviajar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.uji.al394516.aviajar.classes.Travel
import es.uji.al394516.aviajar.recycler.TravelsAdapter

class TravelResultsActivity : AppCompatActivity(), ITravelResults {
    lateinit var layManager: RecyclerView.LayoutManager;
    lateinit var adapter: RecyclerView.Adapter<TravelsAdapter.ViewHolder>;

    lateinit var recycler: RecyclerView;
    lateinit var loadingBar: ProgressBar;

    lateinit var presenter: PresenterTR;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_results)

        recycler = findViewById(R.id.travelsRecycler);
        loadingBar = findViewById(R.id.progressBar);


        presenter = PresenterTR(this, Model(applicationContext))

        setTitle("All travels");
    }


    /**
     * Fills the recycler view with all the data
     *
     * @param  travelsList The array of cocktails to show.
     */
    override fun fillRecyclerView(travelsList: List<Travel>){
        layManager = LinearLayoutManager(this);
        recycler.layoutManager = layManager
        adapter = TravelsAdapter(travelsList, presenter);
        recycler.adapter = adapter;
    }

    /**
     * Hide the progress bar
     */
    override fun hideLoadingBar(){
        loadingBar.visibility = View.INVISIBLE;
    }

    /**
     * Show the recyclerView
     */
    override fun showList(){
        recycler.visibility = View.VISIBLE;
    }

    /**
     * Shows an message via Toast
     * @param message An String of the message
     */
    override fun showMessage(message: String){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * Start the new activity with the selected elements
     */
    override fun startActivity(travel:Travel){
        val intent = Intent(this@TravelResultsActivity,TravelEditionActivity::class.java)
        val trDetail:Travel = travel;
        intent.putExtra("EditMode", true)
        intent.putExtra("CurrentTravel", trDetail)
        startActivity(intent)
    }
}