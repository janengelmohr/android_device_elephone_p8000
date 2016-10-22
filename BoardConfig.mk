# include proprietary libraries and binaries
-include vendor/elephone/p8000/BoardConfigVendor.mk

# Bootloader
TARGET_BOOTLOADER_BOARD_NAME := Auxus_PRIME_201

# Specify MT6753 variant
TARGET_IS_MT6753 := true

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
TARGET_KERNEL_CONFIG := p8000_cyanogenmod_defconfig
BOARD_KERNEL_CMDLINE := bootopt=64S3,32N2,64N2
BOARD_KERNEL_BASE := 0x40078000
BOARD_RAMDISK_OFFSET := 0x03f88000
BOARD_KERNEL_OFFSET := 0x00008000
BOARD_TAGS_OFFSET := 0x0df88000
BOARD_KERNEL_PAGESIZE := 2048
BOARD_MKBOOTIMG_ARGS := --kernel_offset $(BOARD_KERNEL_OFFSET) --ramdisk_offset $(BOARD_RAMDISK_OFFSET) --tags_offset $(BOARD_TAGS_OFFSET)

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
