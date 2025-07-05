package com.example.jetpackcomposeapp.presentation.video_detail

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView

@Composable
fun VideoDetailScreen(videoUrl: String?){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Chi tiết video: $videoUrl")
//        "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
//        "https://live-hls-abr-cdn.livepush.io/live/bigbuckbunnyclip/index.m3u8"
        HlsVideoPlayer(
            url = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
fun HlsVideoPlayer(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Khởi tạo ExoPlayer
    val exoPlayer = remember {
        val mediaItem = MediaItem.fromUri(url)
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(
            mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36",
                "Origin" to "https://livepush.io", // quan trọng
                "Referer" to "https://livepush.io/" // một số server yêu cầu luôn
            )
        )
        val mediaSource =  HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        ExoPlayer.Builder(context).build().apply {
            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true

            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e("ExoPlayer", "Playback error", error)
                }
            })
        }
    }


    // Gắn PlayerView (view truyền thống) vào Compose
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = true // hiện thanh điều khiển
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        },
        modifier = modifier
    )

    // Giải phóng khi Composable bị hủy
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}