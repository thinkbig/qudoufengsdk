cmake_minimum_required(VERSION 3.6)

add_library(libdfsec SHARED
        src/main/cpp/com_qudoufeng_vehiclesdk_jni_NativeHelper.cpp
        src/main/cpp/antidebug.cpp)

find_library(
        # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

target_link_libraries(
        # Specifies the target library.
        libdfsec
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )