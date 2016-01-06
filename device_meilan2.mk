$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# The gps config appropriate for this device
$(call inherit-product, device/common/gps/gps_us_supl.mk)

$(call inherit-product-if-exists, vendor/meizu/meilan2/meilan2-vendor.mk)

DEVICE_PACKAGE_OVERLAYS += device/meizu/meilan2/overlay

ifeq ($(TARGET_PREBUILT_KERNEL),)
	LOCAL_KERNEL := device/meizu/meilan2/prebuilt/kernel
else
	LOCAL_KERNEL := $(TARGET_PREBUILT_KERNEL)
endif

PRODUCT_COPY_FILES += \
	device/meizu/meilan2/rootdir/init.mt6735.rc:root/init.mt6735.rc \
	device/meizu/meilan2/rootdir/init.ssd.rc:root/init.ssd.rc \
	device/meizu/meilan2/rootdir/init.xlog.rc:root/init.xlog.rc \
	device/meizu/meilan2/rootdir/init.mt6735.usb.rc:root/init.mt6735.usb.rc \
	device/meizu/meilan2/rootdir/init.recovery.mt6735.rc:root/init.recovery.mt6735.rc \
	device/meizu/meilan2/rootdir/init.aee.rc:root/init.aee.rc \
	device/meizu/meilan2/rootdir/init.project.rc:root/init.project.rc \
	device/meizu/meilan2/rootdir/init.modem.rc:root/init.modem.rc \
    	device/meizu/meilan2/recovery/root/fstab.mt6753:root/fstab.mt6735  \
	device/meizu/meilan2/rootdir/ueventd.rc:root/ueventd.rc \
	device/meizu/meilan2/rootdir/meta_init.rc:root/meta_init.rc \
	device/meizu/meilan2/rootdir/meta_init.project.rc:root/meta_init.project.rc \
	device/meizu/meilan2/rootdir/meta_init.modem.rc:root/meta_init.modem.rc \
	device/meizu/meilan2/rootdir/meta_init.modem.rc:root/factory_init.rc \
	device/meizu/meilan2/rootdir/meta_init.modem.rc:root/factory_init.project.rc \
	frameworks/native/data/etc/android.software.app_widgets.xml:system/etc/permissions/android.software.app_widgets.xml \
	frameworks/native/data/etc/android.hardware.audio.output.xml:system/etc/permissions/android.hardware.audio.output.xml \
	frameworks/native/data/etc/android.hardware.bluetooth.xml:system/etc/permissions/android.hardware.bluetooth.xml \
	frameworks/native/data/etc/android.hardware.sensor.proximity.xml:system/etc/permissions/android.hardware.sensor.proximity.xml \
	frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
	frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
	frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml \
	frameworks/native/data/etc/android.hardware.location.xml:system/etc/permissions/android.hardware.location.xml \
	frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml \
	frameworks/native/data/etc/android.hardware.touchscreen.multitouch.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.xml

# RIL
PRODUCT_PACKAGES += \
    gsm0710muxd

PRODUCT_PACKAGES += \
    Torch
	
# Wifi
PRODUCT_PACKAGES += \
    libwpa_client \
    hostapd \
    dhcpcd.conf \
    wpa_supplicant \
    wpa_supplicant.conf

PRODUCT_COPY_FILES += \
    device/meizu/meilan2/rootdir/etc/hostapd_default.conf:system/etc/hostapd/hostapd_default.conf \
	
	


$(call inherit-product, build/target/product/full.mk)

#PRODUCT_NAME := full_meilan2
#PRODUCT_DEVICE := meilan2

ADDITIONAL_DEFAULT_PROPERTIES += ro.secure=0 \
ro.allow.mock.location=1 \
persist.mtk.aee.aed=on \
ro.debuggable=1 \
ro.adb.secure=0 \
persist.service.acm.enable=0 \
persist.sys.usb.config=mtp \
ro.mount.fs=EXT4 \
ro.persist.partition.support=no \
ro.cip.partition.support=no \
debug.hwui.render_dirty_regions=false \
ro.sf.lcd_density=320
