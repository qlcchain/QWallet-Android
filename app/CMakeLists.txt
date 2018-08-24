# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#设置生成的so动态库最后输出的路径
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/src/main/jniLibsTest/${ANDROID_ABI})

add_library( # Sets the name of the library.
             qlinkp2p

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
            src/main/java/com/stratagile/qlink/jni/nTox.c
            src/main/java/com/stratagile/qlink/jni/tox.c
            src/main/java/com/stratagile/qlink/jni/Messenger.c
            src/main/java/com/stratagile/qlink/jni/friend_connection.c
            src/main/java/com/stratagile/qlink/jni/DHT.c
            src/main/java/com/stratagile/qlink/jni/crypto_core.c
            src/main/java/com/stratagile/qlink/jni/logger.c
            src/main/java/com/stratagile/qlink/jni/network.c
            src/main/java/com/stratagile/qlink/jni/ping_array.c
            src/main/java/com/stratagile/qlink/jni/LAN_discovery.c
            src/main/java/com/stratagile/qlink/jni/net_crypto.c
            src/main/java/com/stratagile/qlink/jni/TCP_connection.c
            src/main/java/com/stratagile/qlink/jni/TCP_client.c
            src/main/java/com/stratagile/qlink/jni/TCP_server.c
            src/main/java/com/stratagile/qlink/jni/list.c
            src/main/java/com/stratagile/qlink/jni/onion.c
            src/main/java/com/stratagile/qlink/jni/onion_client.c
            src/main/java/com/stratagile/qlink/jni/onion_announce.c
            src/main/java/com/stratagile/qlink/jni/friend_requests.c
            src/main/java/com/stratagile/qlink/jni/group.c
            src/main/java/com/stratagile/qlink/jni/util.c
            src/main/java/com/stratagile/qlink/jni/ping.c
            src/main/java/com/stratagile/qlink/jni/crypto_core_mem.c
            src/main/java/com/stratagile/qlink/jni/tox_api.c
            src/main/java/com/stratagile/qlink/jni/cJSON.c )
             src/main/java/com/stratagile/qlink/c/native-lib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
                                log-lib
                                 #src/main/java/com/stratagile/qlink/sodium

                                 # Specifies the name of the NDK library that
                                 # you want CMake to locate.
                                 log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries( # Specifies the target library.
                       qlinkp2p

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       )