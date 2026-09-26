package com.lukeneedham.videodiary.data.persistence.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * The saved-export table (a name plus a comma-separated list of included dates) was replaced by
 * saved-recap (just a date range - included dates are now derived live from the diary rather than
 * stored). There's no meaningful way to carry old rows over to the new shape, so this drops the
 * old, now-unused table and creates the new one from scratch; every other table is left alone.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `saved_exports`")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `saved_recaps` (" +
                "`id` TEXT NOT NULL, " +
                "`name` TEXT NOT NULL, " +
                "`startDate` TEXT NOT NULL, " +
                "`endDate` TEXT NOT NULL, " +
                "`timestampCreated` INTEGER NOT NULL DEFAULT 0, " +
                "PRIMARY KEY(`id`))"
        )
    }
}
