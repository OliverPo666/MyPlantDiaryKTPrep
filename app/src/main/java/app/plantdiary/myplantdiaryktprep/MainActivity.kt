package app.plantdiary.myplantdiaryktprep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import app.plantdiary.myplantdiaryktprep.ui.main.DiaryFragment
import app.plantdiary.myplantdiaryktprep.ui.main.EventFragment
import app.plantdiary.myplantdiaryktprep.ui.main.MainFragment
import app.plantdiary.myplantdiaryktprep.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var detector : GestureDetectorCompat
    private lateinit var mainFragment : MainFragment
    private lateinit var eventFragment: EventFragment
    private lateinit var activeFragment : DiaryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainFragment = MainFragment.newInstance()
        eventFragment = EventFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
            activeFragment = mainFragment
        }

        detector = GestureDetectorCompat(this, DiaryGestureListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.actionMasked

        return if (detector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }

    }

    inner class DiaryGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            downEvent: MotionEvent?,
            moveEvent: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var diffX = moveEvent?.x?.minus(downEvent!!.x) ?: 0.0F
            var diffY = moveEvent?.y?.minus(downEvent!!.y) ?: 0.0F
            // is it more up/down, or more left/right?
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        this@MainActivity.onSwipeRight()
                    } else {
                        this@MainActivity.onSwipeLeft()
                    }
                }
            } else {
                // up or down
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        this@MainActivity.onSwipeTop()
                    } else {
                        this@MainActivity.onSwipeBottom()
                    }
                }
            }

            return super.onFling(downEvent, moveEvent, velocityX, velocityY)
        }

    }
    private fun onSwipeTop() {
        activeFragment.onSwipeTop()
    }

    private fun onSwipeBottom() {
        activeFragment.onSwipeBottom()
    }

    internal fun onSwipeLeft() {
        Toast.makeText(this, "Swipe Left", Toast.LENGTH_LONG).show()
        if (activeFragment == eventFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
            activeFragment = mainFragment
        }
    }

    internal fun onSwipeRight() {
        Toast.makeText(this, "Swipe Right", Toast.LENGTH_LONG).show()
        if (activeFragment == mainFragment) {
            mainFragment.saveSpecimen()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, eventFragment)
                .commitNow()
            activeFragment = eventFragment
        }

    }
}
