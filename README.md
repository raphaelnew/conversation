# Test Conversation Application (Android)

## Build
Recommended Android Studio 2.2 preview 7.

## Dependencies and Libraries used
- v7 appcompat library [com.android.support:appcompat-v7](http://developer.android.com/tools/support-library/features.html#v7-appcompat) - This library adds support for the Action Bar user interface design pattern. This library includes support for material design user interface implementations.
- v7 recyclerview library [com.android.support:recyclerview-v7](http://developer.android.com/tools/support-library/features.html#v7-recyclerview) - The recyclerview library adds the RecyclerView class.
- constraint-layout [com.android.support.constraint:constraint-layout](http://tools.android.com/tech-docs/layout-editor) - ConstraintLayout type of layout available in the Android Support repository built on top of a flexible constraint system.
- support-annotations [com.android.support:support-annotations](https://developer.android.com/topic/libraries/support-library/features.html#annotations) The Annotation package provides APIs to support adding annotation metadata to apps.
- design Support Library [com.android.support:design](http://developer.android.com/tools/support-library/features.html#design) - The Design package provides APIs to support adding material design components and patterns to app.
- greendao [org.greenrobot:greendao](http://greenrobot.org/greendao) - GreenDAO object oriented interface to data stored in the relational database SQLite. One of the fastest ORM DB for Android.
- retrofit [com.squareup.retrofit2:retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- okhttp3 [com.squareup.okhttp3:okhttp](http://square.github.io/okhttp/) - An HTTP & HTTP/2 client for Android and Java applications.
- picasso [com.squareup.picasso:picasso](https://github.com/square/picasso) - A powerful image downloading and caching library for Android.

- junit [junit:junit]() - Unit test framework.
- robolectric [org.robolectric:robolectric](http://robolectric.org/) - Unit test framework that de-fangs the Android SDK jar. Tests run inside the JVM

## Notes
Most of the "//" comments in the code are only for clarification why I made specific decision, I don't write so much comments in the code usually.
Time spent: 16 hours - all required functionality. + around 3 hours on writing tests/refactoring/optimizations/fixes.

## Tested on devices
- Nexus 6p (stable API 23).
- Nexus 6 (API 24)
- Nexus 5 (API 21)
- Nexus 4 (API 19) emulator provides strange scrolling behaviour because of ConstraintLayout and/or Data Binding.
- Nexus 9 (API 24)
- Nvidia Shield Portable (API 21 hardware keyboard/d-pad navigation).

## TODO
- More unit test coverage, add Instrumentation tests.
- Implement infinite scroll feature, query only limited amount of messages from DB with use of limit and offset query.
- Try implement Rx support from GreenDao (experimental) and update Adapter to use observable.
- Add animations to show/hide searchview.
- Add ability to enter before/after date for search from DatePicker.

## Support
Author: Raphael Gilyazitdinov.
Any questions: iraphaele@gmail.com or rafa@irafa.ru.