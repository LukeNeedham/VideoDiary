package com.lukeneedham.videodiary.data.persistence

import java.io.File

/**
 * A two-phase copy of every video in one folder into another, so that a failure at any point never
 * loses a video:
 * 1. [copy] copies each video into the destination (via a temporary file, so a half-written copy
 * never has a real video name). The sources are not touched.
 * 2. The caller then either [commit]s (deletes the sources) or [rollback]s (deletes the copies).
 *
 * Plain `java.io` only, so it can be unit tested on the JVM.
 */
class VideoFolderTransfer private constructor(
    private val sources: List<File>,
    private val created: List<File>,
    private val sourceSnapshot: Map<String, FileState>,
    private val sourceDir: File,
) {
    data class FileState(val length: Long, val lastModified: Long)

    /** True if the source folder is exactly as it was when it was copied. */
    fun isSourceUnchanged(): Boolean = snapshot(sourceDir) == sourceSnapshot

    /** Deletes the copies this transfer made. Videos which were already in the destination stay. */
    fun rollback() {
        created.forEach { it.delete() }
    }

    /** Deletes the sources. Returns the sources which could not be deleted. */
    fun commit(): List<File> = sources.filterNot { it.delete() }

    companion object {
        const val TEMPORARY_SUFFIX = ".moving"

        /** Deletes half-written copies left behind by a move that was interrupted */
        fun deleteTemporaryFiles(dir: File) {
            dir.listFiles { file -> file.name.endsWith(TEMPORARY_SUFFIX) }?.forEach { it.delete() }
        }

        fun snapshot(dir: File): Map<String, FileState> {
            return videoFiles(dir).associate { it.name to FileState(it.length(), it.lastModified()) }
        }

        /**
         * Copies every video in [from] to [to].
         *
         * If [to] already holds a file with the same name and size, it is treated as an
         * already-copied video. If it holds one with a different size, that is a conflict
         * and this throws, rather than overwrite a video.
         *
         * On any failure the copies made so far are removed and the sources are untouched.
         */
        fun copy(from: File, to: File): VideoFolderTransfer {
            to.mkdirs()
            check(to.isDirectory) { "Could not create destination directory" }
            deleteTemporaryFiles(to)

            val snapshot = snapshot(from)
            val toMove = mutableListOf<File>()
            val created = mutableListOf<File>()
            try {
                videoFiles(from).forEach { source ->
                    val destination = File(to, source.name)
                    if (destination.exists()) {
                        check(destination.length() == source.length()) {
                            "A different video named ${source.name} already exists in the destination"
                        }
                        toMove += source
                        return@forEach
                    }

                    val temporary = File(to, ".${source.name}$TEMPORARY_SUFFIX")
                    try {
                        source.copyTo(temporary, overwrite = true)
                        check(temporary.length() == source.length()) {
                            "Copied video ${source.name} has an unexpected size"
                        }
                        check(temporary.renameTo(destination)) {
                            "Could not finish copying ${source.name}"
                        }
                    } finally {
                        // Does nothing once the rename succeeded
                        temporary.delete()
                    }
                    created += destination
                    toMove += source
                }
            } catch (error: Throwable) {
                created.forEach { it.delete() }
                throw error
            }
            return VideoFolderTransfer(toMove, created, snapshot, from)
        }

        private fun videoFiles(dir: File): List<File> {
            return dir.listFiles { file ->
                file.isFile && !file.name.endsWith(TEMPORARY_SUFFIX)
            }?.toList().orEmpty()
        }
    }
}
