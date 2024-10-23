#include <jni.h>
#include <stdio.h>
#include "jni_example_JNIDemo.h"

JNIEXPORT void JNICALL Java_jni_example_JNIDemo_sayHello(JNIEnv *env, jobject obj) {
    printf("Hello viewers - This method is written in C language!\n");
}

JNIEXPORT void JNICALL Java_jni_example_JNIDemo_fatalError(JNIEnv *env, jobject obj) {
	int arr[2];

    // Accessing out of bound should throw a fatal error
    arr[3] = 10;
}