//package com.weather.app.ui.screens
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.media3.common.MediaItem
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.AspectRatioFrameLayout
//import androidx.media3.ui.PlayerView
//import androidx.media3.common.util.UnstableApi
//
//
//@UnstableApi
//@Composable
//fun VideoBackground(resourceId: Int, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val exoPlayer = remember {
//        ExoPlayer.Builder(context).build().apply {
//            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/$resourceId")
//            setMediaItem(mediaItem)
//            prepare()
//            playWhenReady = true
//            repeatMode = Player.REPEAT_MODE_ALL
//        }
//    }
//
//    DisposableEffect(exoPlayer) {
//        val playerView = PlayerView(context).apply {
//            player = exoPlayer
//            useController = false // Hide the player controls
//            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM // Crop to fill the box
//        }
//
//        onDispose {
//            exoPlayer.release()
//        }
//    }
//
//    AndroidView(
//        factory = { PlayerView(context).apply {
//            player = exoPlayer
//            useController = false // Hide the player controls
//            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM // Crop to fill the box
//        } },
//        modifier = modifier
//    )
//}