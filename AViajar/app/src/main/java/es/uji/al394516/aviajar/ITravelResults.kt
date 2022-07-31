package es.uji.al394516.aviajar

import es.uji.al394516.aviajar.classes.Travel

interface ITravelResults {
    /**
     * Fills the recycler view with all the data
     *
     * @param  travelsList The array of cocktails to show.
     */
    fun fillRecyclerView(travelsList: List<Travel>)

    /**
     * Hide the progress bar
     */
    fun hideLoadingBar()

    /**
     * Show the recyclerView
     */
    fun showList()

    /**
     * Shows an message via Toast
     * @param message An String of the message
     */
    fun showMessage(message: String)

    /**
     * Start the new activity with the selected elements
     */
    fun startActivity(travel:Travel)
}