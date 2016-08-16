#!/bin/bash
cd ../../../..
cd frameworks/base
git apply -v ../../device/elephone/p8000/patches/framework_base/0001-fingerprint_frameworks_base.patch
git apply -v ../../device/elephone/p8000/patches/framework_base/0002-fingerprint_frameworks_base.patch
cd ../..
cd packages/apps/Settings/
git apply -v ../../../device/elephone/p8000/patches/packages_apps_Settings/0001-fingerprint_packages_apps_settings.patch
cd ../../..
cd frameworks/opt/telephony
git apply -v ../../../device/elephone/p8000/patches/framework_opt_telephony/0002_mobile_data_patch_mt6753.patch
cd ../../..
echo Patches Applied Successfully!
