## Specify phone tech before including full_phone
#$(call inherit-product, vendor/cm/config/gsm.mk)

# Release name
PRODUCT_RELEASE_NAME := k05ts_a

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Inherit device configuration
$(call inherit-product, device/alps/k05ts_a/device_k05ts_a.mk)

TARGET_SCREEN_HEIGHT := 1920
TARGET_SCREEN_WIDTH := 1080

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := k05ts_a
PRODUCT_NAME := cm_k05ts_a
PRODUCT_BRAND := alps
PRODUCT_MODEL := k05ts_a
PRODUCT_MANUFACTURER := alps
