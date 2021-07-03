package com.josus.itune.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.josus.itune.R
import com.josus.itune.adapter.MusicAdapter
import com.josus.itune.db.MusicDatabase
import com.josus.itune.repository.MusicRepository
import com.josus.itune.utils.ConnectionManager
import com.josus.itune.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var viewModel: MusicViewModel? = null
    lateinit var musicAdapter: MusicAdapter
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()

        val musicRepository = MusicRepository(MusicDatabase(this))
        val viewModelProviderFactory = MusicViewModelProviderFactory(musicRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MusicViewModel::class.java)


        if(ConnectionManager().checkConnectivity(this)){
            var job: Job? = null
            etSearchSong.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString()
                                .isNotEmpty() && ConnectionManager().checkConnectivity(
                                applicationContext
                            )
                        ) {
                            txtErr.visibility = View.INVISIBLE
                            viewModel?.getSearchMusic(editable.toString())
                        } else {
                            if (editable.toString().isNotEmpty()) {
                                viewModel?.getArtistBySearch(editable.toString())
                                    ?.observe(this@MainActivity,
                                        Observer { artists ->
                                            musicAdapter.differ.submitList(artists)
                                        })
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    R.string.offline_msg,
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel?.getSavedMusic()
                                    ?.observe(this@MainActivity, Observer { musics ->
                                        musicAdapter.differ.submitList(musics)
                                        Log.d(TAG, musics.toString())
                                    })
                            }

                        }
                    }
                }
            }



            try {
                viewModel?.searchedMusics?.observe(this, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { musicResponse ->
                                musicAdapter.differ.submitList(musicResponse.results)
                                Log.d(TAG, response.toString())
                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                Log.d(TAG, "Error:$message")
                                txtErr.visibility = View.VISIBLE
                                txtErr.text = message
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }

            try {
                musicAdapter.setOnItemClickListener {
                    viewModel?.saveMusic(it)

                }

            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
        else{
            Toast.makeText(this,"You are Offline",Toast.LENGTH_LONG).show()
        }

    }

    private fun setUpRecyclerView() {
        musicAdapter = MusicAdapter()
        rvSearchSong.apply {
            adapter = musicAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }
}