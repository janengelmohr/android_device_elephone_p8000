Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/divis1969/android.git -b meilan2

Optinally use a specific manifest (not a tip):

    repo init -u git://github.com/divis1969/android.git -b meilan2 -m cm-12.1-v0.2.xml

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
- Wifi is working
- Camera is working
- Audio is working
- Telephony is working
    - USIM (3G) supported
    - Incoming/outgoung call
    - Data connection (3G/2G)

Known Issues
-------------
- Bluetooth is not functional (not tested)
- Battery charge indicator shows incorrect status (>100 %)
- SIM sometimes is not correctly identiified (workaround: turn Airplane mode on/off)

Change log
----------

### v0.2
- Telephony bring up complete

### v0.1
- Fixed an issue with USB/ADB
- Fixed brightness control and wakeup (screen on) after sleep (with USB detached)
- Fixed headphone is not recognized issue
- Fixed camera freeze when no USB is connected

