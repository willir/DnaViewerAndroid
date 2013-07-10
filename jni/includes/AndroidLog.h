#ifndef __ANDROID_LOG_H__
#define __ANDROID_LOG_H__

#include <android/log.h>

#include <vector>
#include <string>

#define __DEX_TAG__ "DnaReader"

#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, __DEX_TAG__, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, __DEX_TAG__, __VA_ARGS__))
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, __DEX_TAG__, __VA_ARGS__))
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, __DEX_TAG__, __VA_ARGS__))
#define LOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, __DEX_TAG__, __VA_ARGS__))


#endif
