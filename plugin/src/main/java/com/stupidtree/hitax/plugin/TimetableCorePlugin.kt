package com.stupidtree.hitax.plugin

import android.app.Application
import com.stupidtree.hita.api.HostEnvironmentService
import com.stupidtree.hita.api.Plugin
import com.stupidtree.hita.api.PluginContext
import com.stupidtree.hita.api.TimetableService
import com.stupidtree.hitax.data.AppDatabase
import com.stupidtree.hitax.data.repository.SubjectRepository
import com.stupidtree.hitax.data.repository.TimetableRepository

/** T1 timetable core: data, sync codecs and TimetableService; no UI/components. */
class TimetableCorePlugin : Plugin {
    override val id = "com.stupidtree.hitax.timetable"
    override val displayName = "课表核心"
    override val requiresApi = 11

    private var application: Application? = null

    override fun install(ctx: PluginContext) {
        val app = (ctx.service(HostEnvironmentService.KEY)?.applicationContext as? Application)
            ?: error("缺少宿主 applicationContext")
        application = app
        val database = AppDatabase.getDatabase(app)
        val timetables = TimetableRepository.getInstance(app)
        val subjects = SubjectRepository.getInstance(app)
        ctx.provide(TimetableService.KEY, TimetableServiceImpl(timetables, subjects, database))
        ctx.sync.register(TimetableCodec(database.timetableDao()))
        ctx.sync.register(SubjectCodec(database.subjectDao()))
        ctx.sync.register(EventCodec(database.eventItemDao()))
    }

    override fun purgeData() {
        application?.let {
            AppDatabase.reset()
            TimetableRepository.resetInstance()
            SubjectRepository.resetInstance()
        }
    }
}
