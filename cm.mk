# Release name
PRODUCT_RELEASE_NAME := p8000

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Inherit device configuration
$(call inherit-product, device/elephone/p8000/device_p8000.mk)

# Include TWRP part
$(call inherit-product, device/elephone/p8000/twrp.mk)

# Include MultiROM part
$(call inherit-product, device/elephone/p8000/multirom.mk)

# Configure dalvik heap
$(call inherit-product, frameworks/native/build/phone-xxxhdpi-3072-dalvik-heap.mk)

# Configure hwui memory
$(call inherit-product, frameworks/native/build/phone-xxxhdpi-3072-hwui-memory.mk)

TARGET_SCREEN_HEIGHT := 1920
TARGET_SCREEN_WIDTH := 1080

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := p8000
PRODUCT_NAME := cm_p8000
PRODUCT_BRAND := elephone
PRODUCT_MODEL := p8000
PRODUCT_MANUFACTURER := elephone
