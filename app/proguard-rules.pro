-dontobfuscate

-dontwarn okio.**
-keep class okhttp3.internal.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn org.conscrypt.OpenSSLProvider
-dontwarn org.conscrypt.Conscrypt

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-dontwarn com.gianlu.commonutils.Preferences.PreferencesBillingHelper
-dontwarn com.gianlu.commonutils.Preferences.PreferencesBillingHelper$*
-dontwarn com.gianlu.commonutils.Tutorial.BaseTutorial
-dontwarn com.gianlu.commonutils.Tutorial.BaseTutorial$ListenerWrapper
-dontwarn com.gianlu.commonutils.Tutorial.BaseTutorial$Listener
-dontwarn com.gianlu.commonutils.Tutorial.TutorialManager
-dontwarn com.mikepenz.aboutlibraries.ui.item.HeaderItem
-dontwarn com.pavelsikun.vintagechroma.ChromaPreferenceCompat