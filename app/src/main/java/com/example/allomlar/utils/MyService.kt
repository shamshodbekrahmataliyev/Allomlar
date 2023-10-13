package com.mac.allomalar.utils

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.mac.allomalar.repository.Repository
import javax.inject.Inject
import android.provider.Settings

class MyService @Inject constructor(
   private val repository: Repository
) : Service() {
    private var intent: Intent? = Intent()
    private lateinit var player: MediaPlayer

    override fun onBind(p0: Intent?): IBinder? {
        intent = p0
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }


    fun stopIt() {
        stopService(intent)
    }
}