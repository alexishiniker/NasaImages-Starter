package edu.washington.recyclednasaimages

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

class NasaContentDownloader {
    companion object {
        private var mRequestQueue: RequestQueue? = null

        fun requestQueue(c: Context): RequestQueue {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(c)
            }

            return mRequestQueue as RequestQueue
        }
    }
}