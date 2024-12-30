package com.lukeneedham.videodiary.di

import android.net.Uri
import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.ui.RootViewModel
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarViewModel
import com.lukeneedham.videodiary.ui.feature.checkvideo.CheckVideoViewModel
import com.lukeneedham.videodiary.ui.feature.exportdiary.ExportDiaryViewModel
import com.lukeneedham.videodiary.ui.feature.setup.SetupViewModel
import com.lukeneedham.videodiary.ui.feature.videorecorder.RecordVideoViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

object KoinModule {
    val modules = listOf(
        getUtil(),
        getUi(),
        getRepositories(),
        getViewModel(),
        getPersistence(),
    )

    private fun getUtil() = module {
        factory<CoroutineDispatcher>(KoinQualifier.Dispatcher.io) { Dispatchers.IO }
    }

    private fun getUi() = module {
        factory { VideoFileNameMapper() }
        single {
            VideosDao(
                context = androidContext(),
                videoFileNameMapper = get(),
            )
        }
        factory {
            VideoExportDao(
                context = androidContext(),
            )
        }
    }

    private fun getRepositories() = module {
        single {
            CalendarRepository(
                videosDao = get(),
                settingsDao = get(),
            )
        }
        factory {
            VideoResolutionRepository(
                settingsDao = get(),
            )
        }
    }

    private fun getViewModel() = module {
        viewModel {
            RootViewModel(
                settingsDao = get(),
            )
        }
        viewModel {
            SetupViewModel(
                settingsDao = get(),
                ioDispatcher = get(KoinQualifier.Dispatcher.io),
            )
        }
        viewModel {
            RecordVideoViewModel(
                settingsDao = get(),
            )
        }
        viewModel { (date: LocalDate, videoContentUri: Uri) ->
            CheckVideoViewModel(
                date = date,
                videoContentUri = videoContentUri,
                videosDao = get(),
                videoResolutionRepository = get(),
            )
        }
        viewModel {
            CalendarViewModel(
                calendarRepository = get(),
                videoResolutionRepository = get(),
                videosDao = get(),
            )
        }
        viewModel {
            ExportDiaryViewModel(
                videosDao = get(),
                videoExportDao = get(),
                videoResolutionRepository = get(),
            )
        }
    }

    private fun getPersistence() = module {
        /*
        Daos based on Prefs data store need to be singletons,
        since only 1 DataStore can be active at a time for each underlying file.
        Otherwise we get IllegalStateExceptions.
         */
        single {
            SettingsDao(
                context = androidContext(),
            )
        }
    }
}