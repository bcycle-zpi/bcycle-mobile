package pl.pwr.zpi.bcycle.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.yarolegovich.lovelydialog.LovelyStandardDialog
import kotlinx.android.synthetic.main.activity_history_trip_info.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import pl.pwr.zpi.bcycle.mobile.api.ApiClient
import pl.pwr.zpi.bcycle.mobile.models.Trip
import pl.pwr.zpi.bcycle.mobile.ui.dialogs.ShareDialogFragment
import pl.pwr.zpi.bcycle.mobile.utils.*

class HistoryTripInfoActivity : AppCompatActivity(), OnMyMapReadyCallback, ShareDialogFragment.ShareDialogListener, OnPhotosWindowClosedCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var trip: Trip
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var photos: List<String>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_trip_info)
        val tripID = intent.extras!!.getInt(KEY_TRIP_ID)
        getMaps()
        setListeners()
        ApiClient.tripApi.get(tripID).background().subscribe({
            trip = it.result
            photos = trip.photos
            showInfo(trip)
            showRoute(trip.route, mMap, applicationContext)
            animateTo(
                trip.route[trip.route.size / 2].latitude,
                trip.route[trip.route.size / 2].longitude,
                mMap
            )
        }, {
            showToastError(R.string.prompt_unable_to_load_tripinfo)
        })
    }

    private fun setListeners() {
        backBT.setOnClickListener { finish() }
        photosBT.setOnClickListener {
            fragmentContainer.visibility = View.VISIBLE
            if (photos.isNotEmpty()) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragmentContainer, PhotosFragment(
                        this, photos, this
                    )
                ).commit()
            } else {
                showToastWarning(R.string.no_photos)
            }
        }
        shareFAB.setOnClickListener {
            val fragment =
                ShareDialogFragment()
            fragment.arguments = ShareDialogFragment.prepareShareDialog(trip)
            fragment.show(supportFragmentManager, DIALOG_SHARE)
        }
    }

    private fun getMaps() {
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("CheckResult")
    private fun showInfo(trip: Trip) {
        startTV.text = dateToFriendlyString(trip.started)
        endTV.text = dateToFriendlyString(trip.finished)
        durationTV.text = getString(
            R.string.time_format,
            (trip.time / 60).div(60),
            (trip.time / 60).rem(60)
        )
        distanceTV.text = getString(R.string.distance_format, trip.distance)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history_trip, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.trip_remove) {
            LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setTitle(resources.getString(R.string.prompt_remove_trip))
                .setIcon(R.drawable.bike_icon)
                .setPositiveButton(R.string.yes) {
                    showToast(R.string.removing)
                    ApiClient.tripApi.delete(trip.id!!)
                        .background().subscribe({
                            finish()
                        }, { err -> showToastError(R.string.remove_failed) })
                }
                .setPositiveButtonColorRes(R.color.green)
                .setNegativeButton(R.string.no,{})
                .setNegativeButtonColorRes(R.color.red)
                .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun setSharingUrlFromDialog(url: String?) {
        trip.sharingUrl = url
    }

    override fun onPhotosWindowClosed() {
        fragmentContainer.visibility = View.GONE
    }
}