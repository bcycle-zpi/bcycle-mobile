package pl.pwr.zpi.bcycle.mobile.api

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.threeten.bp.ZonedDateTime
import pl.pwr.zpi.bcycle.mobile.API_BASE_URL
import pl.pwr.zpi.bcycle.mobile.dateFromIso
import pl.pwr.zpi.bcycle.mobile.dateToIso
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private val gson = GsonBuilder().registerTypeAdapter(ZonedDateTime::class.java, object :
        TypeAdapter<ZonedDateTime>() {
        override fun read(reader: JsonReader): ZonedDateTime? {
            if (reader.peek() === JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val dtString: String = reader.nextString()
            return dateFromIso(dtString)
        }

        override fun write(writer: JsonWriter, value: ZonedDateTime?) {
            if (value == null) {
                writer.nullValue()
                return
            }
            writer.value(dateToIso(value))
        }
    }).create()

    private var _retrofit: Retrofit? = null
    private var _tripApi: TripApi? = null

    private val retrofit: Retrofit
        get() {
            if (_retrofit == null) {
                _retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()
            }
            return _retrofit!!
        }

    val tripApi: TripApi
        get() {
            if (_tripApi == null) {
                _tripApi = retrofit.create(TripApi::class.java)
            }
            return _tripApi!!
        }

    var currentToken: String = "" // TODO provide a valid token


    private var okHttpClient =
        OkHttpClient().newBuilder().addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val builder: Request.Builder = originalRequest.newBuilder().header(
                "Authorization",
                "Bearer $currentToken"
            )
            val newRequest: Request = builder.build()
            chain.proceed(newRequest)
        }.build()
}