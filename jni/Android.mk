
LOCAL_PATH := $(call my-dir)

# ajax
include $(CLEAR_VARS)
LOCAL_MODULE    := ajax
LOCAL_SRC_FILES := libs/libajax.a
include $(PREBUILT_STATIC_LIBRARY)

# eexpat
include $(CLEAR_VARS)
LOCAL_MODULE    := eexpat
LOCAL_SRC_FILES := libs/libeexpat.a
include $(PREBUILT_STATIC_LIBRARY)

# epcre
include $(CLEAR_VARS)
LOCAL_MODULE    := epcre
LOCAL_SRC_FILES := libs/libepcre.a
include $(PREBUILT_STATIC_LIBRARY)

# ezlib
include $(CLEAR_VARS)
LOCAL_MODULE    := ezlib
LOCAL_SRC_FILES := libs/libezlib.a
include $(PREBUILT_STATIC_LIBRARY)

# dna_viewer
include $(CLEAR_VARS)

LOCAL_MODULE    := dna_viewer
LOCAL_SRC_FILES := \
		main.cpp \

LOCAL_C_INCLUDES += \
		$(LOCAL_PATH)/includes \
		$(LOCAL_PATH)/includes/emboss \

LOCAL_STATIC_LIBRARIES := \
		ajax \
		eexpat \
		epcre \
		ezlib \

LOCAL_LDLIBS    := -llog
LOCAL_CFLAGS += -Wno-psabi

include $(BUILD_SHARED_LIBRARY)
