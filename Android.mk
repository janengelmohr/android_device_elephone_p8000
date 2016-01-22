LOCAL_PATH:= $(call my-dir)

include $(call first-makefiles-under,$(LOCAL_PATH))

include $(CLEAR_VARS)

ifneq ($(TARGET_PREBUILT_KERNEL),)
# Some modules (ex. external/tinycompress depend on $(KERNEL_OUT)/usr,
# but this folder is not created for prebuilt kernel, let'c create it
$(TARGET_OUT_INTERMEDIATES)/KERNEL_OBJ/usr:
	mkdir -p $@
endif
