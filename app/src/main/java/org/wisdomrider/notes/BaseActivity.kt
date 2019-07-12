package org.wisdomrider.notes

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseActivity : com.wisdomrider.Activities.BaseActivity() {
    val BASE_URL = "https://notes.wisdomriderr.shop/api/mobile/"
    lateinit var retrofit: Retrofit
    val gson = Gson()
    var progess: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
//        httpClient.addInterceptor(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//                var original = chain.request()
//
//                var request = original.newBuilder()
//                    .method(original.method(), original.body())
//                    .addHeader(
//                        "User-Agent",
//                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0"
//                    )
//                    .addHeader("Authorization", "Bearer " + preferences.getString("token", ""))
//                    .method(original.method(), original.body())
//                    .build()
//                return chain.proceed(request)
//            }
//        })
        httpClient.addInterceptor(logging)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    fun showAlert(message: String) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage(message)
        alert.show()
    }

    fun closeProgressBar() {
        if (progess != null)
            progess!!.cancel()
    }

    private fun showProgessBar(cont: String) {
        if (progess == null)
            progess = ProgressDialog(this)
        progess!!.setCancelable(false)
        progess!!.setMessage("Loading...")
        progess!!.setTitle(cont)
        progess!!.show()
    }

    public interface Do {
        fun <T> Do(body: T?)
    }

    fun <T> Call<T>.get(
        what: String,
        after: Do? = null,
        messageFor406: String = "Authentication Error",
        messageFor400: String = "Unable to connect to server please try again !",
        onBack: Boolean = false
    ): Any {
        if (!what.isEmpty())
            showProgessBar(what)
        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                closeProgressBar()
                showAlert("Unable to connect to the server.")
                if (onBack) {
                    wisdom.toast("Please check your internet connection.")
                    onBackPressed()
                }

            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                closeProgressBar()
                Log.e("CODE", "" + response.message())
                when (response.code()) {
                    200 -> if (after != null) after.Do(response.body())
                    406 -> showAlert(messageFor406)
                    400 -> showAlert(messageFor400)
                    401 -> {
                        if (!what.isEmpty())
                            showAlert(messageFor406)
//                        else
//                            startActivity(Intent(applicationContext, TrackerPage::class.java))
                    }
                    500 -> showAlert(messageFor400)
                    123 -> showAlert(response.errorBody()!!.string())
                    else -> showAlert("Please check your internet connection.")
                }

            }
        })


        return this
    }
}
