#!/bin/bash
cd ../../../..
cd frameworks/av
git apply -v ../../device/elephone/p8000/patches/framework_av/0001-libmedia-stub-out-MTK-specific-bits-audio-working.patch
#git apply -v ../../device/elephone/p8000/patches/framework_av/0002-camera.patch
cd ../..
cd frameworks/base
git apply -v ../../device/elephone/p8000/patches/framework_base/0001-location-support-GNSS-extension-presumably-from-MTK.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0002-location-add-some-logs-just-to-be-confident.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0003-location-style-fix.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0004-add-com.mediatek-for-build.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0005-Update-for-mtk.patch
git apply -v ../../device/elephone/p8000/Fingerprint/0001-fingerprint_frameworks_base.patch
git apply -v ../../device/elephone/p8000/Fingerprint/0002-fingerprint_frameworks_base.patch
cd ../..
cd packages/apps/Settings/
git apply -v ../../../device/elephone/p8000/Fingerprint/0001-fingerprint_packages_apps_settings.patch
cd ../../..
cd frameworks/opt/telephony
git apply -v ../../../device/elephone/p8000/patches/framework_opt_telephony/0001-Update-for-mtk.patch
git apply -v ../../../device/elephone/p8000/patches/framework_opt_telephony/0002_mobile_data_patch_mt6753.patch
cd ../../..
cd hardware/libhardware
git apply -v ../../device/elephone/p8000/patches/hardware_libhardware/fix-gps-light-audio.patch
cd ../..
cd hardware/libhardware_legacy
git apply -v ../../device/elephone/p8000/patches/hardware_libhardware_legacy/patch-for-mtk.patch
cd ../..
cd packages/services/Telephony
git apply -v ../../../device/elephone/p8000/patches/packages_services_telephony/NeedsFakeIccid.patch
cd ../../..
#cd system/core
#git apply -v ../../device/elephone/p8000/patches/system_core/fix-boot-for-mtk.patch
#cd ../..
cd system/netd
git apply -v ../../device/elephone/p8000/patches/system_netd/hotpost-fix.patch
cd ../..
echo Patches Applied Successfully!
