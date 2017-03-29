Device repository for Meizu M2 Mini (LineageOS)
===========================

Getting Started
---------------

Initialize a repository with LineageOS:

    repo init -u git://github.com/divis1969/android.git -b los-14.1-meilan2

Optinally use a specific manifest (not a tip):

    repo init -u git://github.com/divis1969/android.git -b los-14.1-meilan2 -m los-14.1-meilan2-v0.4.xml

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    make -j 4 bacon showcommands 2>&1 | tee build.log

Current state
-------------

- System boots
- Touch, screen, keyboard, central key are working
- Wifi is working
- Audio is working
- Telephony is working (see Known Issues)
    - USIM (3G) supported
    - Incoming/outgoung call
    - SMS, USSD
    - Data connectivity
- GPS
- Bluetooth
- Sensors
- Camera

Known Issues
-------------
- Meizu Camera App is crasing when switching to front camera
- Android Camera App is crasing when recording video begins
- Hardware OMX codecs are not working

All issues: https://github.com/divis1969/android_device_meizu_meilan2/issues

Change log
----------

### v0.4 (LineageOS)
- Fix A2DP
- Remove Engineering app for now
- Upmerge to the LineageOS cm-14.1 branch tip

### v0.3 (LineageOS)
- Backport LiveDisplay support in kernel from 3.18
- Enable LiveDisplay in HAL
- Upmerge to the LineageOS cm-14.1 branch tip

### v0.2 (LineageOS)
- Build kernel from source code (3.10.65)
- Upmerge to the LineageOS cm-14.1 branch tip

### v0.1 (LineageOS)
- Switch from CyanogenMod to LineageOS (use LineageOS manifest, repositories)
- Fix for offline charging
- Remove -6dB limit for system sounds

### v0.2 (CyanogenMod 14.1)
- Fixed an issue with proximity on some devices
- Fixed an issue with ICC IO in MTK ril (no radio with some SIM cards)
- Fixed a modem crash caused by mtk_agps request
- Fixed an issue with WiFi SoftAP
- Ported power HAL from CyanogenMod 6735 (also implements Double Tap To Wake feature)
- Ported FOTA solution from meilan2 cm-12.1

### v0.1 (CyanogenMod 14.1)
- Initial port from cm-14.0 (v0.3) to cm-14.1

