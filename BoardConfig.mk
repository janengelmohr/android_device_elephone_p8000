# include proprietary libraries and binaries
-include vendor/elephone/p8000/BoardConfigVendor.mk

# use these headers 
TARGET_SPECIFIC_HEADER_PATH := device/elephone/p8000/include
 
# Link against libxlog
TARGET_LDPRELOAD += libxlog.so
 
# Bootloader
TARGET_BOOTLOADER_BOARD_NAME := Auxus_PRIME_201

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
BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2
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

# Bluetooth
BOARD_BLUETOOTH_BDROID_BUILDCFG_INCLUDE_DIR := device/elephone/p8000/bluetooth

# Fingerprint Sensor
VANZO_FEATURE_ADD_SILEADINC_FP := yes
VANZO_FEATURE_FACTORYMODE_USE_ENGLISH := yes

BOARD_EGL_CFG := device/elephone/p8000/configs/egl.cfg

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
