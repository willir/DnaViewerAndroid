#include "CToJavaConverter.h"
#include "AndroidLog.h"

#include "defines.h"

#include "ajmess.h"
#include "ajfile.h"

#include <string>
#include <algorithm>    // std::copy


CToJavaConverter* CToJavaConverter::instance = NULL;
CToJavaConverter::CToJavaConverter(JNIEnv *env) {

	this->jClDnaAbiData = NULL;
	this->jClDnaAbiDataAddInfo = NULL;
	this->jClIntArrClass = NULL;

	this->jFld_DnaAbi_trace = NULL;
	this->jFld_DnaAbi_nseq = NULL;
	this->jFld_DnaAbi_basePositions = NULL;
	this->jFld_DnaAbi_numPoints = NULL;
	this->jFld_DnaAbi_lastNonTrashPoint = NULL;
	this->jFld_DnaAbi_tmax = NULL;
	this->jFld_DnaAbi_basesOrder = NULL;
	this->jFld_DnaAbi_mAddInfo = NULL;
	this->jFld_DnaAbiAddInfo_doubleSignals = NULL;

	this->jm_DnaAbi_init = NULL;
	this->jm_DnaAbiAddInfo_init = NULL;
	//--------------

	if (env->GetJavaVM(&this->jvm) != 0) {
		LOGE("CToJavaConverter env->GetJavaVm(&this->jvm) != 0");
		return;
	}

	jclass jClDnaAbiData = env->FindClass(PACKAGE_C_NAME"/utils/DnaAbiData");
	jclass jClDnaAbiDataAddInfo = env->FindClass(PACKAGE_C_NAME"/utils/DnaAbiData$AddInfo");
	jclass jClIntArrClass = env->FindClass("[I");
	//--------------

	this->jFld_DnaAbi_trace = env->GetFieldID(jClDnaAbiData, "trace", "[[I");
	this->jFld_DnaAbi_nseq = env->GetFieldID(jClDnaAbiData, "nseq", "Ljava/lang/String;");
	this->jFld_DnaAbi_basePositions = env->GetFieldID(jClDnaAbiData, "basePositions", "[S");
	this->jFld_DnaAbi_numPoints = env->GetFieldID(jClDnaAbiData, "numPoints", "I");
	this->jFld_DnaAbi_lastNonTrashPoint = env->GetFieldID(jClDnaAbiData, "lastNonTrashPoint", "I");
	this->jFld_DnaAbi_tmax = env->GetFieldID(jClDnaAbiData, "tmax", "I");
	this->jFld_DnaAbi_basesOrder = env->GetFieldID(jClDnaAbiData, "basesOrder", "Ljava/lang/String;");
	this->jFld_DnaAbi_mAddInfo = env->GetFieldID(jClDnaAbiData, "mAddInfo",
			"L"PACKAGE_C_NAME"/utils/DnaAbiData$AddInfo;");
	this->jFld_DnaAbiAddInfo_doubleSignals = env->GetFieldID(jClDnaAbiDataAddInfo, "doubleSignals",
			"[I");
	//--------------

	this->jm_DnaAbi_init = env->GetMethodID(jClDnaAbiData, "<init>", "()V");
	this->jm_DnaAbiAddInfo_init = env->GetMethodID(jClDnaAbiDataAddInfo, "<init>", "()V");
	//--------------

	this->jClDnaAbiData = (jclass) env->NewGlobalRef(jClDnaAbiData);
	this->jClDnaAbiDataAddInfo = (jclass) env->NewGlobalRef(jClDnaAbiDataAddInfo);
	this->jClIntArrClass = (jclass) env->NewGlobalRef(jClIntArrClass);
	//--------------
}
//********************************************************************************

CToJavaConverter::~CToJavaConverter() {
	JNIEnv *env = this->getEnv();
	if (!env)
		return;

	if (this->jClDnaAbiData)
		env->DeleteGlobalRef(this->jClDnaAbiData);
	if (this->jClDnaAbiDataAddInfo)
		env->DeleteGlobalRef(this->jClDnaAbiDataAddInfo);
	if (this->jClIntArrClass)
		env->DeleteGlobalRef(this->jClIntArrClass);
}
//********************************************************************************

JNIEnv *CToJavaConverter::getEnv() {
	JNIEnv *env;
	if(this->jvm->GetEnv((void **) &env, JNI_VERSION_1_2) != JNI_OK) {
		LOGE("CToJavaConverter::getEnv this->jvm->GetEnv != JNI_OK");
		return NULL;
	}
	return env;
}
//********************************************************************************

jobject CToJavaConverter::abiReaderToJava(AbiReader *abiReader) {
	JNIEnv *env = this->getEnv();
	if (!env)
		return NULL;

	jobject res = env->NewObject(this->jClDnaAbiData, this->jm_DnaAbi_init);

	env->SetObjectField(res, this->jFld_DnaAbi_trace,
			this->get2DArrFromAjPInt2d(abiReader->trace));
	env->SetObjectField(res, this->jFld_DnaAbi_nseq,
			env->NewStringUTF(ajStrGetPtr(abiReader->nseq)));
	env->SetIntField(res, this->jFld_DnaAbi_numPoints, abiReader->numPoints);
	env->SetIntField(res, this->jFld_DnaAbi_lastNonTrashPoint,
			abiReader->lastNonTrashPoint);
	env->SetIntField(res, this->jFld_DnaAbi_tmax, abiReader->tmax);

	env->SetObjectField(res, this->jFld_DnaAbi_basePositions,
			this->getArrFromAjPShort(abiReader->basePositions));

	env->SetObjectField(res, this->jFld_DnaAbi_basesOrder,
			env->NewStringUTF(abiReader->basesOrder));

	env->SetObjectField(res, this->jFld_DnaAbi_mAddInfo,
			this->abiReaderAddInfoTojava(&abiReader->addInfo));

	return res;
}
//********************************************************************************

jobject CToJavaConverter::abiReaderAddInfoTojava(AbiReader::AddInfo *addInfo) {
	JNIEnv *env = this->getEnv();
	if (!env)
		return NULL;

	jobject res = env->NewObject(this->jClDnaAbiDataAddInfo, this->jm_DnaAbiAddInfo_init);
	env->SetObjectField(res, this->jFld_DnaAbiAddInfo_doubleSignals,
			this->getArrFromIntVector(addInfo->doubleSignals));

	return res;
}
//********************************************************************************

CToJavaConverter* CToJavaConverter::getInstance() {
	if(instance == NULL) {
		LOGE("CToJavaConverter::getInstance Illegal State object instance == null");
	}

	return instance;
}
//********************************************************************************

void CToJavaConverter::createInstance(JNIEnv *env) {
	delete instance;
	instance = new CToJavaConverter(env);
}
//********************************************************************************

void CToJavaConverter::destroyInstance() {
	delete instance;
	instance = NULL;
}
//********************************************************************************

jobjectArray CToJavaConverter::get2DArrFromAjPInt2d(AjPInt2d arr) {
	JNIEnv *env = this->getEnv();
	if (!env)
		return NULL;

	ajuint len1, len2;
	ajInt2dLen(arr, &len1, &len2);

	jobjectArray res = env->NewObjectArray((jsize) len1, this->jClIntArrClass,
			NULL);
	// Go through the first dimension and add the second dimension arrays
	for (jsize i = 0; i < len1; i++) {
		jintArray intArr = env->NewIntArray((jsize) len2);
		env->SetIntArrayRegion(intArr, (jsize) 0, (jsize) len2,
				arr->Ptr[i]->Ptr);
		env->SetObjectArrayElement(res, (jsize) i, intArr);
		env->DeleteLocalRef(intArr);
	}

	return res;
}
//********************************************************************************

jshortArray CToJavaConverter::getArrFromAjPShort(AjPShort ajArr) {
	JNIEnv *env = this->getEnv();
	if (!env)
		return NULL;

	jsize len = ajShortLen(ajArr);
	short *arr = ajShortShort(ajArr);
	jshortArray res = env->NewShortArray(len);
	env->SetShortArrayRegion(res, 0, (jsize) len, (short *) arr);

	return res;
}
//********************************************************************************

jintArray CToJavaConverter::getArrFromIntVector(const std::vector<int> &arrVect) {
	JNIEnv *env = this->getEnv();
	if (!env)
		return NULL;

	int *arr = new int[arrVect.size()];
	std::copy(arrVect.begin(), arrVect.end(), arr);

	jintArray res = env->NewIntArray(arrVect.size());
	env->SetIntArrayRegion(res, 0, arrVect.size(), arr);

	delete[] arr;

	return res;
}
//********************************************************************************
