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
