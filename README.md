Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/CyanogenMod/android.git -b cm-12.1

Create local manifest for meizu meilan2 repositories:

    mkdir -p .repo/local_manifests/meizu_m2.xml

Populate the meizu_m2.xml with the following content:

    <?xml version="1.0" encoding="UTF-8"?>
    <manifest>
      <remove-project name="platform/frameworks/testing" />
      <remove-project name="CyanogenMod/android_system_core" />
      <project path="system/core" name="divis1969/android_system_core" revision="meilan2" />
      <project path="device/meizu/meilan2" name="divis1969/android_device_meizu_meilan2" />
      <project path="vendor/meizu/meilan2" name="divis1969/android_vendor_meizu_meilan2" revision="master" />
    </manifest>

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    mka bacon 2>&1 | tee build.log

Flash the phone:
https://github.com/divis1969/android_device_meizu_meilan2/wiki/%D0%9F%D1%80%D0%BE%D1%88%D0%B8%D0%B2%D0%BA%D0%B0-%D1%82%D0%B5%D0%BB%D0%B5%D1%84%D0%BE%D0%BD%D0%B0


Current state
-------------

- Recovery is fully functional
- Update package (OTA) generation
- Cyanogen boots
- Touch, screen, keyboard, central key are working
- Wifi working
- Camera is working
- Audio is working
- MTK RILD starts, comminicate to modem, SIM

Issues
-------------
- Bluetooth is not functional
- Radio (modem) is not functional (cannot register in network)
- USB (ADB+MTP) is re-connecting after ~60 seconds

