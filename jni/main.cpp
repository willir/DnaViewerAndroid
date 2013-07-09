/**
 * This file contain function, which will be call from java.
 */


#include <jni.h>

#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <string>

#include "AndroidLog.hpp"

#include "ajarch.h"
#include "ajarr.h"
#include "ajstr.h"
#include "ajseqabi.h"
#include "ajmess.h"
#include "ajfile.h"


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

JNIEXPORT jint JNICALL
Java_ru_willir_dnaviewer_utils_DnaViewNative_test1(JNIEnv* env, jobject thiz, jstring filePathJ) {
	string filePath = jstringToStdString(env, filePathJ);
	AjPFile   fp             = NULL;
	const char *fname = NULL;

	LOGD("test1:%s", filePath.c_str());

	fp = ajFileNewInNameC(filePath.c_str());
    fname = ajFileGetPrintnameC(fp);

	LOGD("test1 fname:%s", fname);

	return 0;
}

}
