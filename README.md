Device repository for Meizu M2 Mini (CyanogenMod)
===========================

Getting Started
---------------

Initialize a repository with CyanogenMode:

    repo init -u git://github.com/divis1969/android.git -b cm-13.0

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    make -j 4 bacon showcommands 2>&1 | tee build.log

Flash the phone:
https://github.com/divis1969/android_device_meizu_meilan2/wiki/%D0%9F%D1%80%D0%BE%D1%88%D0%B8%D0%B2%D0%BA%D0%B0-%D1%82%D0%B5%D0%BB%D0%B5%D1%84%D0%BE%D0%BD%D0%B0

Current state
-------------

- Update package generation
- Cyanogen boots
- Touch, screen, keyboard, central key are working
- Wifi is working

Known Issues
-------------
All issues: https://github.com/divis1969/android_device_meizu_meilan2/issues

Change log
----------

