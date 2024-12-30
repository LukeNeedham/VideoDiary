package com.lukeneedham.videodiary.ui.media

import androidx.media3.common.Player

object VideoPlayerPool {
    private val players = mutableSetOf<Player>()
    private var playerPlayingBeforeGlobalPause: Set<Player> = emptySet()

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun removePlayer(player: Player) {
        players.remove(player)
    }

    fun onAppPause() {
        playerPlayingBeforeGlobalPause = players.filter { it.isPlaying }.toSet()
        players.forEach {
            it.pause()
        }
    }

    fun onAppResume() {
        playerPlayingBeforeGlobalPause.forEach {
            it.play()
        }
        playerPlayingBeforeGlobalPause = emptySet()
    }
}