/**
 * This file contain function, which will be call from java.
 */


#include <jni.h>

#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <string>

#include "AndroidLog.h"
#include "AbiReader.h"
#include "CToJavaConverter.h"

using namespace std;

extern "C" {

string jstringToStdString(JNIEnv* env, jstring str) {
	if(str == NULL)
		return "";

    int len = env->GetStringUTFLength(str);
    const char* arr = env->GetStringUTFChars(str, JNI_FALSE);
    string res(arr, len);
    env->ReleaseStringUTFChars(str, arr);

    return res;
}

JNIEXPORT jobject JNICALL
Java_ru_willir_dnaviewer_utils_DnaViewNative_test1(JNIEnv* env, jobject thiz, jstring filePathJ) {
	CToJavaConverter::createInstance(env);
	CToJavaConverter *cToJava = CToJavaConverter::getInstance();

	string filePath = jstringToStdString(env, filePathJ);

	LOGD("test1:%s", filePath.c_str());

	AbiReader abiReader(filePath);

	LOGD("is ok:%d", abiReader.isSequenceOk());

	jobject res = cToJava->abiReaderToJava(&abiReader);

	CToJavaConverter::destroyInstance();

	return res;
}

}
