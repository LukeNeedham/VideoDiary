package com.lukeneedham.videodiary.data.persistence

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class VideoFolderTransferTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var from: File
    private lateinit var to: File

    @Before
    fun setUp() {
        from = tempFolder.newFolder("from")
        to = File(tempFolder.root, "to")
    }

    private fun File.write(name: String, text: String) = File(this, name).apply { writeText(text) }

    @Test
    fun copyThenCommitMovesEveryVideo() {
        from.write("a", "aaa")
        from.write("b", "bb")

        val transfer = VideoFolderTransfer.copy(from, to, failOnConflict = true)
        assertEquals("aaa", File(to, "a").readText())
        assertTrue("Sources are untouched until commit", File(from, "a").exists())

        assertTrue(transfer.commit().isEmpty())
        assertEquals(emptyList<String>(), from.list()!!.toList())
        assertEquals(listOf("a", "b"), to.list()!!.sorted())
    }

    @Test
    fun rollbackRemovesCopiesAndKeepsSources() {
        from.write("a", "aaa")

        VideoFolderTransfer.copy(from, to, failOnConflict = true).rollback()

        assertEquals("aaa", File(from, "a").readText())
        assertEquals(emptyList<String>(), to.list()!!.toList())
    }

    @Test
    fun conflictFailsAndLeavesNothingBehind() {
        from.write("a", "aaa")
        from.write("b", "bbb")
        to.mkdirs()
        to.write("b", "different length")

        try {
            VideoFolderTransfer.copy(from, to, failOnConflict = true)
            fail("Expected a conflict")
        } catch (e: IllegalStateException) {
            // expected
        }

        assertEquals(listOf("a", "b"), from.list()!!.sorted())
        // Only the destination's own file remains: no partial copy of "a", no temporary files
        assertEquals(listOf("b"), to.list()!!.toList())
        assertEquals("different length", File(to, "b").readText())
    }

    @Test
    fun conflictIsKeptAsIsWhenNotFailing() {
        from.write("a", "aaa")
        from.write("b", "bbb")
        to.mkdirs()
        to.write("b", "different length")

        val transfer = VideoFolderTransfer.copy(from, to, failOnConflict = false)
        transfer.commit()

        assertEquals("different length", File(to, "b").readText())
        assertEquals("aaa", File(to, "a").readText())
        assertEquals("The conflicting source must not be deleted", listOf("b"), from.list()!!.toList())
    }

    @Test
    fun alreadyCopiedVideoIsNotAConflictAndIsNotRolledBack() {
        from.write("a", "aaa")
        to.mkdirs()
        to.write("a", "xyz")

        val transfer = VideoFolderTransfer.copy(from, to, failOnConflict = true)
        transfer.rollback()

        assertTrue("Rollback must not delete a file it did not create", File(to, "a").exists())
    }

    @Test
    fun staleTemporaryFilesDoNotBlockAndAreCleanedUp() {
        from.write("a", "aaa")
        to.mkdirs()
        to.write(".a${VideoFolderTransfer.TEMPORARY_SUFFIX}", "half")

        VideoFolderTransfer.copy(from, to, failOnConflict = true)

        assertEquals(listOf("a"), to.list()!!.toList())
    }

    @Test
    fun temporaryFilesInSourceAreNotMoved() {
        from.write(".a${VideoFolderTransfer.TEMPORARY_SUFFIX}", "half")

        VideoFolderTransfer.copy(from, to, failOnConflict = true).commit()

        assertEquals(emptyList<String>(), to.list()!!.toList())
    }

    @Test
    fun detectsSourceChangedAfterCopy() {
        from.write("a", "aaa")
        val transfer = VideoFolderTransfer.copy(from, to, failOnConflict = true)
        assertTrue(transfer.isSourceUnchanged())

        from.write("new", "recorded during the copy")
        assertFalse(transfer.isSourceUnchanged())
    }
}
