ifeq ($(strip $(VANZO_FEATURE_ADD_SILEADINC_FP)), yes)
#############################
# Copyright Silead
# By Warren Zhao
#############################
LOCAL_PATH := $(call my-dir)


#############################################
##FOR LOCAL INSTALLED LIBS
#############################################
#one install solution not turned on here since it can not handle non-lib/exe files. 
#and the test files will be installed under /system/bin which is not good.
#alternative solution is put as one the product config file on device directory.
#include $(CLEAR_VARS)

#module, including the path relative to root
define wz_include_module_rule
  
LOCAL_SRC_FILES_64 := $(1)
LOCAL_MODULE := $(notdir $(1))
LOCAL_MODULE_CLASS := EXECUTABLES
LOCAL_MODULE_TAGS := optional
include $(BUILD_PREBUILT)
   
endef

define wz_add_module_rule

    $(eval $(wz_include_module_rule))
  
endef

define wz_add_modules_rules
  
    $(foreach m,$(1),$(call wz_add_module_rule,$m))

endef

#maybe the path need special handling upto how android handles it.
#LOCAL_PREBUILT_LIBS := $(shell ls $(LOCAL_PATH)/target/lib)
#include $(BUILD_MULTI_PREBUILT)

#
#slfp_intall_files := $(shell ls $(LOCAL_PATH)/target/bin)
#slfp_intall_files := $(addprefix $(LOCAL_PATH)/target/bin/, $(slfp_intall_files))
#$(eval $(call wz_add_modules_rules,$(slfp_intall_files)))

#
#slfp_intall_files := $(shell ls $(LOCAL_PATH)/target/test)
#slfp_intall_files := $(addprefix $(LOCAL_PATH)/target/test/, $(slfp_intall_files))
#$(eval $(call wz_add_modules_rules,$(slfp_intall_files)))

#############################################
##FOR LOCAL EXTERNAL LIBS
#############################################
#local dependent libs
#module, including the path relative to root

define wz_include_lib_rule

include $(CLEAR_VARS) 
LOCAL_MODULE := $(notdir $(1))
LOCAL_SRC_FILES_64 := $(1)
LOCAL_MODULE_CLASS := $(2)
LOCAL_MODULE_TAGS := optional

$(LOCAL_MODULE) : $(LOCAL_PATH)/$(1)

$(LOCAL_PATH)/$(1)	:	
						make -C $(SL_ROOT_DIR) all

include $(BUILD_PREBUILT)

include $(CLEAR_VARS)

LOCAL_MODULE := $(basename $(notdir $(1)))
LOCAL_SRC_FILES_64 := $(1)
LOCAL_MODULE_CLASS := $(2)
LOCAL_MODULE_TAGS := optional
LOCAL_UNINSTALLABLE_MODULE := true

include $(BUILD_PREBUILT)

$(basename $(notdir $(1))) : $(notdir $(1))
   
endef

define wz_add_lib_rule

    $(eval $(call wz_include_lib_rule,$(1),$(2)))
  
endef

#input is relative path to LOCAL_PATH
define wz_add_libs_rules
  
    $(foreach m,$(1),$(call wz_add_lib_rule,$m,$(2)))

endef

#############################

WZ_LOCAL_EXTERNAL_SHARED_LIBS := libfpcal.so
$(eval $(call wz_add_libs_rules,$(WZ_LOCAL_EXTERNAL_SHARED_LIBS),SHARED_LIBRARIES))

############################

include $(CLEAR_VARS)
LOCAL_PROGUARD_ENABLED:=disabled
LOCAL_MODULE := libsileadinc_dev
LOCAL_SRC_FILES_64 := sileadinc_dev_jni.c

LOCAL_LDFLAGS := -Wl,--no-undefined #-Wl,--no-allow-shlib-undefined 
#LOCAL_CFLAGS := 
LOCAL_C_INCLUDES := $(LOCAL_PATH)/inc

LOCAL_SHARED_LIBRARIES_64 := $(basename $(notdir $(WZ_LOCAL_EXTERNAL_SHARED_LIBS)))
#LOCAL_LDLIBS += -lfpsvcd_remoteapi #this does not work due to android make system bug when using TARGET_CUSTOM_LD_COMMAND
#LOCAL_LDLIBS += -llog 

LOCAL_PRELINK_MODULE := false

#APP_OPTIM := debug
LOCAL_SHARED_LIBRARIES += liblog 

LOCAL_JNI_SHARED_LIBRARIES := libsileadinc_dev
#LOCAL_PROGUARD_ENABLED := disabled
LOCAL_MODULE_TAGS := optional

include $(BUILD_SHARED_LIBRARY)

endif
