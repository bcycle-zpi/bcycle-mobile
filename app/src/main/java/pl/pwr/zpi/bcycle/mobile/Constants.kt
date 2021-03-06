package pl.pwr.zpi.bcycle.mobile

const val API_BASE_URL = "https://bcycle.azurewebsites.net/api/"
const val HTTP_TIMEOUT_S: Long = 20
const val M_TO_KM: Double = 1000.0
const val MS_TO_S: Double = 1000.0
const val UPCOMING_GROUP_TRIP_OFFSET_MINUTES: Long = 30
const val LOCATION_UPDATE_INTERVAL_MS: Long = 5000
// notifications
const val NOTIFICATION_CHANNEL_ONGOING_ID = "ongoing"
const val ONGOING_NOTIFICATION_ID = 1

// intent names and extras
const val INTENT_LOCATION_UPDATE = "pl.pwr.zpi.bcycle.mobile.LOCATION_UPDATE"
const val INTENT_START_TRIP = "pl.pwr.zpi.bcycle.mobile.START_TRIP"
const val INTENT_EXTRA_MAIN_NAV_ID = "navID"
const val INTENT_EXTRA_SHARE_ISGROUP = "tripIsGroupTrip"
const val INTENT_EXTRA_SHARE_ID = "tripID"
const val INTENT_EXTRA_SHARE_PHOTO_COUNT = "tripPhotoCount"
const val INTENT_EXTRA_SHARE_URL = "tripShareURL"
const val INTENT_EXTRA_INVITE_TRIP_NAME = "tripName"
const val INTENT_EXTRA_INVITE_CODE = "tripCode"
const val INTENT_EXTRA_RECORD_GROUP_TRIP_ID = "groupTripId"
const val INTENT_EXTRA_IS_EDITING = "isEditing"
const val INTENT_EXTRA_EDITED_TRIP = "editedTrip"
const val DIALOG_SHARE = "ShareDialog"
const val DIALOG_INVITE = "InviteDialog"

// startActivityForResult request codes
const val REQUEST_CODE_AFTER_GOOGLE_PLAY = 101

//formats
val TIME_FORMAT = "HH:mm"
val DATE_FORMAT = "yyyy-MM-dd"
val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"

//keys for bundle between trip creation activities
val START_DATE_KEY = "START_DATE_KEY"
val END_DATE_KEY = "END_DATE_KEY"
val NAME_KEY = "NAME_KEY"
val DESCRIPTION_KEY = "DESCRIPTION_KEY"
val KEY_TRIP_ID = "KEY_TRIP_ID"

const val PERMISSIONS_REQUEST_LOCAITON = 102
const val PERMISSIONS_REQUEST_CAMERA_STORAGE = 103

const val PICK_IMAGE_REQUEST = 1
const val MAKE_IMAGE_REQUEST = 2

const val MAP_POLYLINE_WIDTH_MIN = 3f
const val MAP_POLYLINE_WIDTH_MIN_AT = 10f
const val MAP_POLYLINE_WIDTH_MAX = 18f
const val MAP_POLYLINE_WIDTH_MAX_AT = 20f
const val MAP_POLYLINE_WIDTH_RANGE = MAP_POLYLINE_WIDTH_MAX_AT - MAP_POLYLINE_WIDTH_MIN_AT
const val MAP_POLYLINE_WIDTH_GROWTH = (MAP_POLYLINE_WIDTH_MAX - MAP_POLYLINE_WIDTH_MIN) / MAP_POLYLINE_WIDTH_RANGE
