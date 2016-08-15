$(call inherit-product, device/mediatek/mt6753_common/device_mt6753.mk)
$(call inherit-product, vendor/elephone/p8000/p8000-vendor.mk)

DEVICE_PACKAGE_OVERLAYS += device/elephone/p8000/overlay

# Device uses high-density artwork where available
PRODUCT_AAPT_CONFIG := normal xhdpi xxhdpi
PRODUCT_AAPT_PREF_CONFIG := xxhdpi

# Recovery allowed devices
TARGET_OTA_ASSERT_DEVICE := p8000,k05ts_a

ifeq ($(TARGET_PREBUILT_KERNEL),)
	LOCAL_KERNEL := device/elephone/p8000/prebuilt/kernel
else
	LOCAL_KERNEL := $(TARGET_PREBUILT_KERNEL)
endif

# init.rc's
PRODUCT_COPY_FILES += \
	device/elephone/p8000/rootdir/init.mt6735.rc:root/init.mt6735.rc \
	device/elephone/p8000/rootdir/init.ssd.rc:root/init.ssd.rc \
	device/elephone/p8000/rootdir/init.xlog.rc:root/init.xlog.rc \
	device/elephone/p8000/rootdir/init.rc:root/init.rc \
	device/elephone/p8000/rootdir/init.mt6735.usb.rc:root/init.mt6735.usb.rc \
	device/elephone/p8000/rootdir/init.recovery.mt6735.rc:root/init.recovery.mt6735.rc \
	device/elephone/p8000/rootdir/init.project.rc:root/init.project.rc \
	device/elephone/p8000/rootdir/init.modem.rc:root/init.modem.rc \
	device/elephone/p8000/recovery/root/fstab.mt6753:root/fstab.mt6735  \
	device/elephone/p8000/rootdir/ueventd.mt6735.rc:root/ueventd.mt6735.rc \
	device/elephone/p8000/rootdir/factory_init.rc:root/factory_init.rc \
	device/elephone/p8000/rootdir/factory_init.project.rc:root/factory_init.project.rc \
	device/elephone/p8000/rootdir/meta_init.project.rc:root/meta_init.project.rc \
	device/elephone/p8000/rootdir/meta_init.modem.rc:root/meta_init.modem.rc \
	device/elephone/p8000/rootdir/meta_init.rc:root/meta_init.rc 

# TWRP thanks to Hanuma50
PRODUCT_COPY_FILES += device/elephone/p8000/recovery/twrp.fstab:recovery/root/etc/twrp.fstab

# Fingerprint support
PRODUCT_PACKAGES += fp
PRODUCT_PACKAGES += slfpcal
PRODUCT_PACKAGES += libslfpjni
PRODUCT_PACKAGES += libsileadinc_dev

include device/elephone/p8000/Fingerprint/slfpcal/Android.mk
include device/elephone/p8000/Fingerprint/fp/Android.mk


PRODUCT_COPY_FILES += \
    	device/elephone/p8000/rootdir/etc/hostapd_default.conf:system/etc/hostapd/hostapd_default.conf \

# limit dex2oat threads to improve thermals
PRODUCT_PROPERTY_OVERRIDES += \
    	dalvik.vm.boot-dex2oat-threads=4 \
    	dalvik.vm.dex2oat-threads=2 \
    	dalvik.vm.image-dex2oat-threads=4

$(call inherit-product, build/target/product/full.mk)
