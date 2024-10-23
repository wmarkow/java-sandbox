# Script to build the JNI example.
# Tested under Cygwin.
# Tested with Amazon Corretto JDK1.8.0_392
# Tested with mingw64-x86_64-gcc-core Cygwin package.

mkdir -p bin

javac -h . jni/example/JNIDemo.java

x86_64-w64-mingw32-gcc -Wl,--add-stdcall-alias -shared -o ./bin/JNIDemo.dll JNIDemo.c -I${JAVA_HOME}/include -I${JAVA_HOME}/include/win32 -I${JAVA_HOME}/include/darwin