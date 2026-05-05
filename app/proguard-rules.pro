# Add project specific ProGuard rules here.
# You can control the default rules by using the -dontobfuscate, -dontoptimize,
# -dontshrink, and -dontwarn options.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Room persistence library
-keepnames class * extends androidx.room.RoomDatabase
-keep public class * extends androidx.room.Dao
-keep class androidx.room.RoomWarnings { public <fields>; public <methods>; }
-keepattributes Signature

# Dagger Hilt
-keep class dagger.hilt.android.internal.managers.HiltController
-keep class dagger.hilt.android.internal.modules.HiltWrapper_WorkerModule
-keep class dagger.hilt.android.internal.builders.ActivityComponentBuilder
-keep class dagger.hilt.android.internal.builders.FragmentComponentBuilder
-keep class dagger.hilt.android.internal.builders.ViewComponentBuilder
-keep class dagger.hilt.android.internal.builders.ServiceComponentBuilder
-keep class dagger.hilt.android.internal.builders.BroadcastReceiverComponentBuilder
-keep class dagger.hilt.android.internal.builders.ViewModelComponentBuilder
-keep class dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder

-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.ads.AdActivity

# WorkManager
-keep class androidx.work.impl.workers.** { *; }
-keep class androidx.work.impl.utils.** { *; }
-keep class androidx.work.impl.background.systemjob.** { *; }
-keep class androidx.work.impl.background.greedy.** { *; }
-keep class androidx.work.** { *; }

# Keep names of @Entity, @Dao, @TypeConverter classes and methods
-keep @interface androidx.room.Entity
-keep @interface androidx.room.Dao
-keep @interface androidx.room.TypeConverter
-keepclassmembers class ** {
    @androidx.room.Entity <fields>;
    @androidx.room.Dao <fields>;
    @androidx.room.TypeConverter <methods>;
}

# Keep enum constants for Room TypeConverters if using them for enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# For Kotlinx Coroutines
-dontwarn kotlinx.coroutines.flow.internal.*

# For Java 8 Time API (LocalDate, LocalTime, DayOfWeek, etc.)
# No specific rules needed usually, as these are part of Android's core library desugaring.
# If issues arise, consider adding specific rules for java.time classes, but typically not required.