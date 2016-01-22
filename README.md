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
      <project path="device/meizu/meilan2" name="divis1969/android_device_meizu_meilan2" />
      <project path="vendor/meizu/meilan2" name="divis1969/android_vendor_meizu_meilan2" revision="master" />
    </manifest>

Build the code:

    source build/envsetup.sh
    breakfast meilan2
    mka 2>&1 | tee build.log

Current state
-------------

Just compiled
