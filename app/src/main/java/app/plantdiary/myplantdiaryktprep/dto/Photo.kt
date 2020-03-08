package app.plantdiary.myplantdiaryktprep.dto

import android.net.Uri
import java.util.*

data class Photo(var localUri: String = "", var remoteUri: String = "", var description: String = "", var dateTaken : Date? = null, var id : String = "")