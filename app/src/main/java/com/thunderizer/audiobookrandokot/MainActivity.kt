package com.thunderizer.audiobookrandokot

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private val basedir = "storage" + File.separator + "6135-6630" + File.separator + "books" + File.separator
    private var initialized = false
    lateinit var booktitle : TextView
    private var mediaPlayer : MediaPlayer = MediaPlayer()
    private val minutes = 3
    private var timeForNewSong : Long = System.currentTimeMillis() + (minutes*60*1000)
    private var timeleft : Long = timeForNewSong / 1000

    private val handler = Handler()

    var dir : File = File(basedir)
    var books : Array<String>? = dir.list()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        booktitle = findViewById(R.id.booktitle)

        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 1000)
                if (!initialized)
                    return
                if (System.currentTimeMillis() >= timeForNewSong) {
                    timeForNewSong = System.currentTimeMillis() + (minutes*60*1000)
                    randomizeSpot(null)
                }
                timeleft = (timeForNewSong - System.currentTimeMillis()) / 1000
                booktitle.text = timeleft.toString() + " seconds left"
            }
        })
    }

    private fun loadSong(book : String?) {
        val uri = Uri.parse(basedir + book)
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer.start()
    }

    fun randomizeSpot(view: View?) {
        Toast.makeText(applicationContext, "Randomizing...", Toast.LENGTH_SHORT).show()
        val r = Random()
        val which = r.nextInt(books!!.size)
        if (initialized) {
            mediaPlayer.reset()
        } else {
            initialized = true
        }
        loadSong(books?.get(which))
        val place = r.nextInt(mediaPlayer.duration)
        mediaPlayer.seekTo(place)
    }



}