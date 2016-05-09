ifeq ($(strip $(VANZO_FEATURE_ADD_SILEADINC_FP)), yes)
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_JAVA_LIBRARIES := #bouncycastle telephony-common
LOCAL_STATIC_JAVA_LIBRARIES := #guava android-support-v4 jsr305

LOCAL_MODULE_TAGS := optional

LOCAL_PROGUARD_ENABLED:= disabled

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := slfpcal
LOCAL_CERTIFICATE := platform
ifeq ($(strip $(VANZO_FEATURE_FACTORYMODE_USE_ENGLISH)), yes)
$(shell if [ -e $(LOCAL_PATH)/vanzo-english ];then cp -rf $(LOCAL_PATH)/vanzo-english/* $(LOCAL_PATH)/res/values/; fi)
endif
ifeq ($(strip $(VANZO_FEATURE_FACTORYMODE_CHANGE_LANGUAGE)),yes)
$(shell if [ -e $(LOCAL_PATH)/vanzo-english ];then mkdir -p $(LOCAL_PATH)/res/values-zh-rCN && cp -rf $(LOCAL_PATH)/res/values/strings_test.xml $(LOCAL_PATH)/res/values-zh-rCN && cp -rf $(LOCAL_PATH)/vanzo-english/* $(LOCAL_PATH)/res/values/;fi)
endif

#LOCAL_PROGUARD_FLAG_FILES := proguard.flags

include $(BUILD_PACKAGE)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
endif
