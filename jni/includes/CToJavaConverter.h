#ifndef __C_TO_JAVA_CONVERTER__
#define __C_TO_JAVA_CONVERTER__

#include <jni.h>
#include <stdint.h>
#include <AbiReader.h>

class CToJavaConverter {
public:

	~CToJavaConverter();

	/**
	 * Get existing instance of this class.
	 * Before called this method, must be called method createInstance.
	 *
	 * @return Instance of this class (singleton).
	 */
	static CToJavaConverter* getInstance();

	/**
	 * Create new instance of this object.
	 * And save it into JavaCaller::instance variable.
	 *
	 * @param jvm
	 */
	static void createInstance(JNIEnv *env);

	/**
	 * Destroy current singleton if it exist.
	 */
	static void destroyInstance();

	/**
	 * @return {@link ru.willir.dnaviewer.utils.DnaAbiData} object.
	 */
	jobject abiReaderToJava(AbiReader *abiReader);

private:
	static CToJavaConverter* instance;

	jclass jClDnaAbiData;
	jclass jClDnaAbiDataAddInfo;
	jclass jClIntArrClass;

	jfieldID jFld_DnaAbi_trace;
	jfieldID jFld_DnaAbi_nseq;
	jfieldID jFld_DnaAbi_basePositions;
	jfieldID jFld_DnaAbi_numPoints;
	jfieldID jFld_DnaAbi_lastNonTrashPoint;
	jfieldID jFld_DnaAbi_tmax;
	jfieldID jFld_DnaAbi_basesOrder;
	jfieldID jFld_DnaAbi_mAddInfo;
	jfieldID jFld_DnaAbiAddInfo_doubleSignals;


	jmethodID jm_DnaAbi_init;
	jmethodID jm_DnaAbiAddInfo_init;

	JavaVM *jvm;

	CToJavaConverter(JNIEnv *env);

	/**
	 * @return pointer to JNIEnv.
	 */
	JNIEnv *getEnv();

	jobject abiReaderAddInfoTojava(AbiReader::AddInfo *addInfo);

	jobjectArray get2DArrFromAjPInt2d(AjPInt2d arr);
	jshortArray getArrFromAjPShort(AjPShort ajArr);
	jintArray getArrFromIntVector(const std::vector<int> &arr);
};

#endif
