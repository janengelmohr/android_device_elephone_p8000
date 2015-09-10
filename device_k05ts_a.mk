$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# The gps config appropriate for this device
$(call inherit-product, device/common/gps/gps_us_supl.mk)

$(call inherit-product-if-exists, vendor/alps/k05ts_a/k05ts_a-vendor.mk)

DEVICE_PACKAGE_OVERLAYS += device/alps/k05ts_a/overlay
LOCAL_PATH := device/alps/k05ts_a

ifeq ($(TARGET_PREBUILT_KERNEL),)
	LOCAL_KERNEL := $(LOCAL_PATH)/prebuilt/kernel
else
	LOCAL_KERNEL := $(TARGET_PREBUILT_KERNEL)
endif

PRODUCT_COPY_FILES += \
    $(LOCAL_KERNEL):kernel \
    $(LOCAL_PATH)/recovery/root/init.recovery.mt6735.rc:root/init.recovery.mt6735.rc \

PRODUCT_COPY_FILES_OVERRIDES += \
    recovery/root/file_contexts \
    recovery/root/property_contexts \
    recovery/root/seapp_contexts \
    recovery/root/sepolicy \
    recovery/root/ueventd.rc

$(call inherit-product, build/target/product/full.mk)

PRODUCT_BUILD_PROP_OVERRIDES += BUILD_UTC_DATE=0
PRODUCT_NAME := full_k05ts_a
PRODUCT_DEVICE := k05ts_a
PRODUCT_MODEL := P8000
PRODUCT_BRAND := Elephone
