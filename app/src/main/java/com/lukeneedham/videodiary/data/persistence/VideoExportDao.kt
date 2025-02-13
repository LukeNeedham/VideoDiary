package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.mp4parser.muxer.Movie
import org.mp4parser.muxer.Track
import org.mp4parser.muxer.builder.DefaultMp4Builder
import org.mp4parser.muxer.container.mp4.MovieCreator
import org.mp4parser.muxer.tracks.AppendTrack
import java.io.File


class VideoExportDao(
    context: Context,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    private val outputDir = File(context.filesDir, "export").apply {
        mkdirs()
    }
    private val outputFile = File(outputDir, outputFileName)

    // Even though the export is synchronous,
    // we force it to be run as a coroutine since it can take considerable time.
    // We run it on the default dispatcher, which is best suited for CPU intensive work,
    // video encoding is a perfect use-case for this.
    suspend fun export(videos: List<File>): File = withContext(defaultDispatcher) {
        exportSync(videos)
    }

    private fun exportSync(videos: List<File>): File {
        // Delete existing export if it exists -
        // there should be at most 1 export file at any time, to avoid clutter
        outputFile.delete()

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

        outputFile.outputStream().use {
            mp4file.writeContainer(it.channel)
        }

        return outputFile
    }

    companion object {
        const val outputFileName = "export.mp4"
    }
}