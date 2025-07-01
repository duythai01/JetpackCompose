package com.example.jetpackcomposeapp.presentation.video_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VideoDetailScreen(videoUrl: String?){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Chi tiết video: $videoUrl")
    }
}

@Composable
fun YoutubePlayer(){

}