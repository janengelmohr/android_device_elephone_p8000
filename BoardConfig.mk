# include proprietary libraries and binaries
-include vendor/elephone/p8000/BoardConfigVendor.mk

# use these headers 
TARGET_SPECIFIC_HEADER_PATH := device/elephone/p8000/include
 
# Link against libxlog
TARGET_LDPRELOAD += libxlog.so
 
# Bootloader
TARGET_BOOTLOADER_BOARD_NAME := Auxus_PRIME_201
TARGET_NO_BOOTLOADER := true
 
# 1st architecture aarch64
TARGET_BOARD_PLATFORM := mt6753
TARGET_ARCH := arm64
TARGET_NO_BOOTLOADER := true
TARGET_CPU_ABI := arm64-v8a
TARGET_CPU_ABI2 :=
TARGET_ARCH_VARIANT := armv8-a
TARGET_CPU_VARIANT := generic
TARGET_BOARD_SUFFIX := _64
TARGET_USES_64_BIT_BINDER := true
TARGET_IS_64_BIT := true
TARGET_CPU_CORTEX_A53 := true
# 2nd architecture arm
TARGET_2ND_ARCH := arm
TARGET_2ND_ARCH_VARIANT := armv7-a-neon
TARGET_2ND_CPU_ABI := armeabi-v7a
TARGET_2ND_CPU_ABI2 := armeabi
TARGET_2ND_CPU_VARIANT := cortex-a53

# Architecture Extensions
ARCH_ARM_HAVE_NEON := true
ARCH_ARM_HAVE_VFP := true
ARCH_ARM_HAVE_TLS_REGISTER := true
TARGET_CPU_SMP := true

# ABI lists for build.prop
TARGET_CPU_ABI_LIST_64_BIT := $(TARGET_CPU_ABI)
TARGET_CPU_ABI_LIST_32_BIT := $(TARGET_2ND_CPU_ABI),$(TARGET_2ND_CPU_ABI2)
TARGET_CPU_ABI_LIST := $(TARGET_CPU_ABI_LIST_64_BIT),$(TARGET_CPU_ABI_LIST_32_BIT)

# CFlags
TARGET_GLOBAL_CFLAGS   += -mfpu=neon -mfloat-abi=softfp
TARGET_GLOBAL_CPPFLAGS += -mfpu=neon -mfloat-abi=softfp
COMMON_GLOBAL_CFLAGS += -DNO_SECURE_DISCARD
COMMON_GLOBAL_CFLAGS += -DDISABLE_HW_ID_MATCH_CHECK
TARGET_USERIMAGES_USE_EXT4 := true

# needed for mass storage mode
TARGET_USE_CUSTOM_LUN_FILE_PATH := /sys/class/android_usb/android0/f_mass_storage/lun/file
  
#extracted from /proc/partinfo
BOARD_BOOTIMAGE_PARTITION_SIZE := 16777216
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 16777216
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 2147483648
BOARD_USERDATAIMAGE_PARTITION_SIZE := 1107296256
BOARD_CACHEIMAGE_PARTITION_SIZE := 444596224
BOARD_FLASH_BLOCK_SIZE := 131072

# build kernel from source
TARGET_KERNEL_SOURCE := kernel/elephone/p8000
TARGET_KERNEL_ARCH := arm64
TARGET_KERNEL_HEADER_ARCH := arm64
TARGET_KERNEL_CONFIG := p8000_cyanogenmod12_1_defconfig
TARGET_KERNEL_CROSS_COMPILE_PREFIX := aarch64-linux-android-
BOARD_KERNEL_IMAGE_NAME := Image.gz-dtb
BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2 selinux=0
BOARD_KERNEL_BASE := 0x40078000
BOARD_RAMDISK_OFFSET := 0x03f88000
BOARD_KERNEL_OFFSET := 0x00008000
BOARD_TAGS_OFFSET := 0x0df88000
BOARD_KERNEL_PAGESIZE := 2048
BOARD_MKBOOTIMG_ARGS := --kernel_offset $(BOARD_KERNEL_OFFSET) --ramdisk_offset $(BOARD_RAMDISK_OFFSET) --tags_offset $(BOARD_TAGS_OFFSET)
MTK_APPENDED_DTB_SUPPORT := yes

# Build an EXT4 ROM image
TARGET_USERIMAGES_USE_EXT4 := true
TARGET_NO_FACTORYIMAGE := true

# system.prop
TARGET_SYSTEM_PROP := device/elephone/p8000/system.prop

# CyanogenMod Hardware Hooks
BOARD_HARDWARE_CLASS := device/elephone/p8000/cmhw/

# WiFi
#WPA_SUPPLICANT_VERSION := VER_0_8_X
#BOARD_HOSTAPD_DRIVER := NL80211
#BOARD_HOSTAPD_PRIVATE_LIB := lib_driver_cmd_mt66xx
#BOARD_WPA_SUPPLICANT_DRIVER := NL80211
#BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_mt66xx
#WIFI_DRIVER_FW_PATH_PARAM:="/dev/wmtWifi"
#WIFI_DRIVER_FW_PATH_STA:=STA
#WIFI_DRIVER_FW_PATH_AP:=AP
#WIFI_DRIVER_FW_PATH_P2P:=P2P

# RIL
#BOARD_CONNECTIVITY_VENDOR := MediaTek
#BOARD_USES_LEGACY_MTK_AV_BLOB := true

# Bluetooth
#BOARD_HAVE_BLUETOOTH := true
#BOARD_HAVE_BLUETOOTH_MTK := true
#BOARD_BLUETOOTH_DOES_NOT_USE_RFKILL := true
#BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := device/elephone/p8000/bluetooth

# GPS
#BOARD_GPS_LIBRARIES :=true
#BOARD_CONNECTIVITY_MODULE := conn_soc
#BOARD_MEDIATEK_USES_GPS := true

# Camera
USE_CAMERA_STUB := true

# Audio
TARGET_CPU_MEMCPY_OPT_DISABLE := true
BOARD_USES_MTK_AUDIO := true

# FM Radio
MTK_FM_SUPPORT := yes
MTK_FM_RX_SUPPORT := yes

# Mediatek flags
BOARD_HAS_MTK_HARDWARE := true
MTK_HARDWARE := true
COMMON_GLOBAL_CFLAGS += -DMTK_HARDWARE -DADD_LEGACY_ACQUIRE_BUFFER_SYMBOL
COMMON_GLOBAL_CPPFLAGS += -DMTK_HARDWARE

# Fingerprint Sensor
VANZO_FEATURE_ADD_SILEADINC_FP := yes
VANZO_FEATURE_FACTORYMODE_USE_ENGLISH := yes

# EGL settings
USE_OPENGL_RENDERER := true
BOARD_EGL_CFG := device/elephone/p8000/egl.cfg
BOARD_EGL_WORKAROUND_BUG_10194508 := true

# Block based ota
# see http://review.cyanogenmod.org/#/c/78849/1/core/Makefile
BLOCK_BASED_OTA := false

# recovery
#TARGET_RECOVERY_INITRC := device/elephone/p8000/recovery/init.mt6753.rc
TARGET_RECOVERY_FSTAB := device/elephone/p8000/recovery/root/fstab.mt6753
TARGET_RECOVERY_LCD_BACKLIGHT_PATH := \"/sys/devices/platform/leds-mt65xx/leds/lcd-backlight/brightness\"

# use power button for selections in recovery
BOARD_HAS_NO_SELECT_BUTTON := true

# ________________________________________________TWRP_________________________________________________
# RECOVERY_VARIANT := twrp

TW_THEME := portrait_hdpi
# brightness settings (needs verification)
TW_BRIGHTNESS_PATH := /sys/devices/platform/leds-mt65xx/leds/lcd-backlight/brightness/
TW_MAX_BRIGHTNESS := 255
# may be useful if we get graphical glitches
RECOVERY_GRAPHICS_USE_LINELENGTH := true
# in case of wrong color this needs modification
TARGET_RECOVERY_PIXEL_FORMAT := "RGBX_8888"
# if sdcard0 is a /data/media emulated one
RECOVERY_SDCARD_ON_DATA := true
# ntfs support? (needs much space..)
TW_INCLUDE_NTFS_3G := true
# we may need that if sdcard0 dont work
TW_FLASH_FROM_STORAGE := true
TW_EXTERNAL_STORAGE_PATH := "/external_sd"
TW_EXTERNAL_STORAGE_MOUNT_POINT := "external_sd"
TW_DEFAULT_EXTERNAL_STORAGE := true
# name backup folders 'p8000' and not after MTK's fake hardware ID '1234567...'
TW_USE_MODEL_HARDWARE_ID_FOR_DEVICE_ID := true
# we have it and it's enforcing!
TWHAVE_SELINUX := true
#only add if kernel supports
#TW_INCLUDE_FUSE_EXFAT := true
#F2FS support (only activate if kernel supports)
#TARGET_USERIMAGES_USE_F2FS:=true
# encryption
TW_INCLUDE_CRYPTO := true
# Antiforensic wipe
BOARD_SUPPRESS_SECURE_ERASE :=  true
# CPU temp
TW_CUSTOM_CPU_TEMP_PATH := /sys/devices/virtual/thermal/thermal_zone1/temp
