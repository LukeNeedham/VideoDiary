package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import org.mp4parser.muxer.Movie
import org.mp4parser.muxer.Track
import org.mp4parser.muxer.builder.DefaultMp4Builder
import org.mp4parser.muxer.container.mp4.MovieCreator
import org.mp4parser.muxer.tracks.AppendTrack
import java.io.File


class VideoExportDao(
    private val context: Context,
) {
    private val outputDir = File(context.filesDir, "export").apply {
        mkdirs()
    }

    fun export(videos: List<File>): File {
        val movies = videos.map { video ->
            MovieCreator.build(video.path)
        }

        // Extract all audio tracks and all video tracks
        val audioTracks = mutableListOf<Track>()
        val videoTracks = mutableListOf<Track>()
        movies.forEach { movie ->
            movie.tracks.forEach { track ->
                val handler = track.handler
                when (handler) {
                    "soun" -> audioTracks.add(track)
                    "vide" -> videoTracks.add(track)
                }
            }
        }

        val movie = Movie()
        // Append all audio tracks together, and all video tracks together,
        // so they play sequentially,
        // while the audio and video play simultaneously.
        movie.addTrack(AppendTrack(*audioTracks.toTypedArray()))
        movie.addTrack(AppendTrack(*videoTracks.toTypedArray()))

        val mp4file = DefaultMp4Builder().build(movie)

        val outputFileName = System.currentTimeMillis().toString() + ".mp4"
        val outputFile = File(outputDir, outputFileName)

        outputFile.outputStream().use {
            mp4file.writeContainer(it.channel)
        }

        return outputFile
    }
}