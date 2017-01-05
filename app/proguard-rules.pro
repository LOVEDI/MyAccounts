# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AS_SDK\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#指定代码的压缩级别
-optimizationpasses 5

#混淆时是否要记录日志。指定在处理期间写出一些更多信息。 如果程序以异常终止，则此选项将打印整个堆栈跟踪，而不仅仅是异常消息
-verbose

#忽略警告
-ignorewarning

-keep class com.beicai.zhaodi.accountbook.database.dao.AccountBookDAO{
        public void onCreate(android.database.sqlite.SQLiteDatabase);
}
-keep class com.beicai.zhaodi.accountbook.database.dao.CategoryDAO{
        public void onCreate(android.database.sqlite.SQLiteDatabase);
}
-keep class com.beicai.zhaodi.accountbook.database.dao.UserDAO{
        public void onCreate(android.database.sqlite.SQLiteDatabase);
}
-keep class com.beicai.zhaodi.accountbook.database.dao.PayoutDAO{
        public void onCreate(android.database.sqlite.SQLiteDatabase);
}
-keep class com.beicai.zhaodi.accountbook.database.dao.CreateViewDAO{
       *;
}
-keep class org.achartengine.**{*;}

#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature
#移除log assumenosideeffects表示忽略它的作用
-assumenosideeffects class android.util.Log{
	public static int v(java.lang.String,java.lang.String);
	public static int d(java.lang.String,java.lang.String);
	public static int i(java.lang.String,java.lang.String);
}