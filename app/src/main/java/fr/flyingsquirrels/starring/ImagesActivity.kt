package fr.flyingsquirrels.starring

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.fragment_image.*




class ImagesActivity : AppCompatActivity() {
    private val mHideHandler = Handler()
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_images)

        ViewCompat.setTransitionName(images_viewpager, EXTRA_IMAGE)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.extras.getString(EXTRA_TITLE)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        images_viewpager.setOnClickListener { toggle() }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        images_viewpager.setOnTouchListener(mDelayHideTouchListener)

        val adapter = ImagesAdapter(supportFragmentManager, intent.extras.getStringArray(EXTRA_URL), intent.extras.getByteArray(EXTRA_THUMBNAIL))
        images_viewpager.adapter = adapter
        images_viewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                page_label.text="${position+1}/${adapter.count}"
            }
        })
        page_label.text="1/${adapter.count}"

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(AUTO_HIDE_DELAY_MILLIS)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
    }

    private fun hide() {
        // Hide UI first
        hideActionBar()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    private fun show() {
        mVisible = true

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        showActionBar()
        fullscreen_content_controls.visibility = View.VISIBLE
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                prepareFinish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        prepareFinish()
    }

    private fun prepareFinish() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportFinishAfterTransition()
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        const val EXTRA_THUMBNAIL = "thumbnail"
        const val EXTRA_URL = "url"
        const val EXTRA_TITLE = "title"
        const val EXTRA_IMAGE = "image"
    }

    class ImagesAdapter( fragmentManager: FragmentManager, private val images: Array<String>, private val thumbnail: ByteArray) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return if(position==0){
                ImageFragment.newInstance(images[0],thumbnail)
            }else{
                ImageFragment.newInstance(images[position], null)
            }
        }

        override fun getCount() = images.size

    }


    class ImageFragment : Fragment() {
        private var type: String? = null
        private var url: String? = null
        private var thumbnail: ByteArray? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                url = it.getString(EXTRA_URL)
                thumbnail = it.getByteArray(EXTRA_THUMBNAIL)
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_image, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            thumbnail?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                image.setImageBitmap(bitmap)
            }
            Picasso.get().load(url).fit().centerInside().placeholder(image.drawable).into(image)

            image.setOnPhotoTapListener { _, _, _ ->
                (activity as ImagesActivity).toggle()
            }
        }

        companion object {
            @JvmStatic
            fun newInstance(url: String, thumbnail: ByteArray?) =
                    ImageFragment().apply {
                        arguments = Bundle().apply {
                            putString(EXTRA_URL, url)
                            putByteArray(EXTRA_THUMBNAIL, thumbnail)
                        }
                    }

            const val EXTRA_URL = "url"
            const val EXTRA_THUMBNAIL = "thumbnail"
        }
    }

    private fun hideActionBar() {
        val ab = supportActionBar
        if (ab != null && ab.isShowing) {
            if (toolbar != null) {
                toolbar.animate()
                        .translationY(-112f)
                        .setDuration(200L)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .withEndAction { ab.hide() }
                        .start()
            } else {
                ab.hide()
            }
        }
    }

    private fun showActionBar() {
        val ab = supportActionBar
        if (ab != null && !ab.isShowing) {
            ab.show()
            if (toolbar != null) {
                toolbar.animate()
                        .translationY(0f)
                        .setDuration(200L)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .start()
            }
        }
    }

}



