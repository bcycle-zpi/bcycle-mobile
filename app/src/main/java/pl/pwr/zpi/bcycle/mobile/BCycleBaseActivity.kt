package pl.pwr.zpi.bcycle.mobile

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class BCycleBaseActivity: AppCompatActivity() {
    protected fun showPermissionExplanationDialog(
        andThen: () -> Unit,
        permissionTitle: Int,
        permissionMessage: Int
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialog = builder
            .setMessage(permissionMessage)
            .setTitle(permissionTitle)
            .setPositiveButton(android.R.string.ok) { _, _ -> andThen() }
            .create()
        dialog.show()
    }

    protected fun ensurePermissions(
        andThen: () -> Unit,
        permissions: Array<String>,
        permissionTitle: Int,
        permissionMessage: Int,
        permissionsRequestCode: Int,
        ignoreRationale: Boolean = false
    ) {
        var canRequest = true
        var allGranted = true
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(this, perm)
                != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
                if (!ignoreRationale && ActivityCompat.shouldShowRequestPermissionRationale(this, perm) && canRequest) {
                    showPermissionExplanationDialog(
                        { ensurePermissions(andThen, permissions, permissionTitle,
                            permissionMessage, permissionsRequestCode, true) },
                        permissionTitle, permissionMessage
                    )
                    canRequest = false
                }
            }
        }
        if (allGranted) andThen.invoke()
        else if (canRequest)
            ActivityCompat.requestPermissions(
                this,
                permissions,
                permissionsRequestCode
            )
    }

    fun openGroupTrip(id: Int) {
        val intent = Intent(applicationContext, FutureTripInfoActivity::class.java)
        intent.putExtra(KEY_TRIP_ID, id)
        startActivity(intent)
    }

    fun openPrivateTrip(id: Int) {
        val intent = Intent(applicationContext, HistoryTripInfoActivity::class.java)
        intent.putExtra(KEY_TRIP_ID, id)
        startActivity(intent)
    }

    fun startTrip(groupTripId: Int? = null) {
        val intent = Intent(applicationContext, RecordTripActivity::class.java)
        if (groupTripId != null) {
            intent.putExtra(INTENT_EXTRA_RECORD_GROUP_TRIP_ID, groupTripId)
        }
        startActivity(intent)
    }
}
