package com.stupidtree.hitax.plugin

import android.app.Application
import com.stupidtree.hita.api.Plugin
import com.stupidtree.hita.api.PluginContext
import com.stupidtree.hita.api.TimetableService
import com.stupidtree.hitax.data.AppDatabase
import com.stupidtree.hitax.data.repository.SubjectRepository
import com.stupidtree.hitax.data.repository.TimetableRepository

/** T1 timetable core: data, sync codecs and TimetableService; no UI/components. */
class TimetableCorePlugin(private val application: Application) : Plugin {
    override val id = "com.stupidtree.hitax.timetable"
    override val displayName = "课表核心"
    override val requiresApi = 10

    override fun install(ctx: PluginContext) {
        val database = AppDatabase.getDatabase(application)
        val timetables = TimetableRepository.getInstance(application)
        val subjects = SubjectRepository.getInstance(application)
        ctx.provide(TimetableService.KEY, TimetableServiceImpl(timetables, subjects, database))
        ctx.sync.register(TimetableCodec(database.timetableDao()))
        ctx.sync.register(SubjectCodec(database.subjectDao()))
        ctx.sync.register(EventCodec(database.eventItemDao()))
    }

    override fun purgeData() {
        AppDatabase.reset()
        TimetableRepository.resetInstance()
        SubjectRepository.resetInstance()
    }
}
