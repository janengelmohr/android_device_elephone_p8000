# android_device_alps_k05ts_a
Device repository for Elephone P8000 CM12.1

Currently broken features:
 * Smart Cover

## Thanks to:
 * DerTeufel1980
 * fire855
 * jianminglok
 * walter79
 * valascus
 * Restorn


##How to compile:
1. Get some free space on your harddisk (better to use a SSD) - 14gb download + 60gb for one build + 50gb ccache
2. Get the latest CM 12.1 sources.
3. Download my manifest [from here](https://github.com/visi0nary/Elephone_P8000_manifest) and put it under .repo/local_manifests
4. Repo sync again so the device tree, kernel and vendor repos are pulled.
5. Apply all patches from device/elephone/p8000/patches to CM sources. The folder names correspond to the folders in CM sources.
6. (Optional) Make sure you run a stock ROM (11/05/15 recommended) or an already existing CM 12.1 version. Run ./extract-files.sh from the device tree while your phone is connected to USB and has ADB enabled. This will pull all pre-compiled binaries and put it under /vendor/elephone/p8000. You can also skip this step and use my vendor_elephone_p8000 repo but that repo is updated less regular than this one.
7. (Optional) Set up CCache if you want to build regularly to speed up future compilations.
8. Go to the root of CM and run ". build/envsetup.sh" followed by "lunch cm_p8000-user" and "mka bacon". If you want a debug build, swap "user" with "userdebug" or "eng".
9. Go grab yourself a coffee. A full build with empty CCache takes around 5 hours on my machine (i5 Dualcore with Hyperthreading, 8 GB RAM and SSD).
10. Once it's finished you can just flash it. Congratulations, you built your own CM 12.1 for the P8000!
11. Contribute to the project if you find a bug or want to improve something :) Feel free to play around with the sources or use parts of it for your own project. Just make sure you give credits and everything is alright.
12. optional: if yo get this error: 'android_errorWriteWithInfoLog' was not declared in this scope
install this patch: https://android.googlesource.com/platform/system/core/+/75ac84c0bf57d646dfae468916fcdcc071570293%5E!/
