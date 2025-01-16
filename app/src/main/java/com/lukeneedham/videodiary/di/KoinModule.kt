package com.lukeneedham.videodiary.di

import android.net.Uri
import com.lukeneedham.videodiary.data.android.PermissionChecker
import com.lukeneedham.videodiary.data.mapper.VideoFileNameMapper
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideoExportDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.CalendarRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarViewModel
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.ExportDiaryCreateViewModel
import com.lukeneedham.videodiary.ui.feature.exportdiary.view.ExportDiaryViewViewModel
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionsViewModel
import com.lukeneedham.videodiary.ui.feature.record.check.CheckVideoViewModel
import com.lukeneedham.videodiary.ui.feature.record.film.RecordVideoViewModel
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationViewModel
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationViewModel
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionViewModel
import com.lukeneedham.videodiary.ui.root.RootViewModel
import com.lukeneedham.videodiary.ui.share.Sharer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

object KoinModule {
    val modules = listOf(
        getUtil(),
        getData(),
        getRepositories(),
        getViewModel(),
        getPersistence(),
    )

    private fun getUtil() = module {
        factory<CoroutineDispatcher>(KoinQualifier.Dispatcher.io) { Dispatchers.IO }
        factory<CoroutineDispatcher>(KoinQualifier.Dispatcher.default) { Dispatchers.Default }
        factory {
            Sharer(
                context = androidContext(),
            )
        }
    }

    private fun getData() = module {
        factory { VideoFileNameMapper() }
        factory {
            PermissionChecker(
                context = androidContext(),
            )
        }
        single {
            VideosDao(
                context = androidContext(),
                videoFileNameMapper = get(),
            )
        }
        factory {
            VideoExportDao(
                context = androidContext(),
                defaultDispatcher = get(KoinQualifier.Dispatcher.default),
            )
        }
    }

    private fun getRepositories() = module {
        single {
            CalendarRepository(
                videosDao = get(),
                videoFileNameMapper = get(),
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
                permissionChecker = get(),
            )
        }
        viewModel {
            RequestPermissionsViewModel(
                permissionChecker = get(),
            )
        }
        viewModel {
            SetupSelectResolutionViewModel(
                settingsDao = get(),
                ioDispatcher = get(KoinQualifier.Dispatcher.io),
            )
        }
        viewModel {
            SetupSelectOrientationViewModel(
                settingsDao = get(),
                ioDispatcher = get(KoinQualifier.Dispatcher.io),
            )
        }
        viewModel {
            SelectVideoDurationViewModel(
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
            ExportDiaryCreateViewModel(
                videoExportDao = get(),
                calendarRepository = get(),
            )
        }
        viewModel {
            ExportDiaryViewViewModel(
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