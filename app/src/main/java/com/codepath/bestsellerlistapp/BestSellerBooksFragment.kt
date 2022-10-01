package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import com.codepath.bestsellerlistapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

// --------------------------------//
// CHANGE THIS TO BE YOUR API KEY  //
// --------------------------------//
private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    /*
     * Constructing the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        // Using the client, perform the HTTP request
        client[
                "https://api.themoviedb.org/3/movie/now_playing",
                params,
                object :
                    JsonHttpResponseHandler()
        // Uncomment me once you complete the above sections!
        { //connect these callbacks to your API call
            /*
             * The onSuccess function gets called when
             * HTTP response status is "200 OK"
             */
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JSON
            ) {
                // The wait for a response is over
                progressBar.hide()

                //TODO - Parse JSON into Models
               // Log.d("DEBUG ARRAY", json.jsonArray.toString())
                val resultsJSON : JSONArray = json.jsonObject.get("results") as JSONArray
        //        Log.d("DEBUG ARRAY", resultsJSON.toString())
                val booksRawJSON : String = resultsJSON.toString()
/*               //val booksRawJSON : String = resultsJSON.get("results").toString()

               //val resultsJSON : JSONObject = json.jsonArray.get("results") as JSONObject
               val booksRawJSON : String = resultsJSON.get("title").toString()
*/
               val gson = Gson()
               val arrayMovieType = object : TypeToken<List<BestSellerBook>>() {}.type
         //      val models : List<BestSellerBook> = gson.fromJson(booksRawJSON, arrayBookType)
               val models : List<BestSellerBook> = gson.fromJson(booksRawJSON,arrayMovieType)
               recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

               //val models : List<BestSellerBook> = null // Fix me!

         //      recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

               // Look for this in Logcat:
               Log.d("BestSellerBooksFragment", "response successful")
               //Log.d("BestSellerBooksFragment", json.toString())
           }

           /*
            * The onFailure function gets called when
            * HTTP response status is "4XX" (eg. 401, 403, 404)
            */
           override fun onFailure(
               statusCode: Int,
               headers: Headers?,
               errorResponse: String,
               t: Throwable?
           ) {
               // The wait for a response is over
               progressBar.hide()

               // If the error is not null, log it!
               t?.message?.let {
                   Log.e("BestSellerBooksFragment", errorResponse)
               }
           }
       }]
   }

   /*
    * What happens when a particular book is clicked.
    */
   override fun onItemClick(item: BestSellerBook) {
       Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
   }

}
