package fr.flyingsquirrels.starring

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image.*


private const val EXTRA_URL = "url"
private const val EXTRA_THUMBNAIL = "thumbnail"

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
        Picasso.with(context).load(url).fit().centerInside().placeholder(image.drawable).into(image)

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
    }
}
