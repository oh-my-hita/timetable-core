# Timetable Core (T1)

T1 timetable service for OH-MY-HITA.

- Provides `TimetableService`
- Owns Room database, DAO, ICS import and sync codecs
- Contains no Activity, Fragment, Widget, resources or Compose UI
- Depends on `oh-my-hita/plugin-sdk:10.1`

Build:

```bash
./gradlew :plugin:buildPluginDex
```

Output: `plugin/build/plugin-dex/classes.dex`

Status: T1 core prototype. UI T2 and host service wiring are still pending.
