#!/bin/bash
cd ../../../..
cd frameworks/av
#working
git apply -v ../../device/elephone/p8000/patches/framework_av/0001-libmedia-stub-out-MTK-specific-bits-audio-working.patch
#git apply -v ../../device/elephone/p8000/patches/framework_av/0002-camera.patch
cd ../..
cd frameworks/base
#working
git apply -v ../../device/elephone/p8000/patches/framework_base/0001-location-support-GNSS-extension-presumably-from-MTK.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0002-location-add-some-logs-just-to-be-confident.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0003-location-style-fix.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0006-add-com.mediatek-for-build.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0007-Update-for-mtk.patch
cd ../..
cd frameworks/opt/telephony
#working
git apply -v ../../../device/elephone/p8000/patches/framework_opt_telephony/0001-Update-for-mtk.patch
git apply -v ../../../device/elephone/p8000/patches/framework_opt_telephony/0002_mobile_data_patch_mt6753.patch
cd ../../..
#
#working
cd hardware/libhardware
git apply -v ../../device/elephone/p8000/patches/hardware_libhardware/0001-fix-gps-light-audio.patch
#
cd ../..
cd hardware/libhardware_legacy
#working
git apply -v ../../device/elephone/p8000/patches/hardware_libhardware_legacy/0001-patch-for-mtk.patch
cd ../..
cd packages/services/Telephony
#working
git apply -v ../../../device/elephone/p8000/patches/packages_services_Telpheony/0001-Update-for-mtk.patch
cd ../../..
cd system/core
#working
git apply -v ../../device/elephone/p8000/patches/system_core/0001-fix-boot-for-mtk.patch
cd ../..
cd system/netd
#working
git apply -v ../../device/elephone/p8000/patches/system_netd/0001-hotpost-fix.patch
cd ../..
echo Patches Applied Successfully!
