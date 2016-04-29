/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <system/window.h>
#include <ui/PixelFormat.h>
#include <ui/Rect.h>
#include <gui/SurfaceControl.h>

namespace android {

// ---------------------------------------------------------------------------

extern "C" {

    /* status_t GraphicBufferMapper::lock */
    status_t _ZN7android19GraphicBufferMapper4lockEPK13native_handleiRKNS_4RectEPPv(
            buffer_handle_t handle, int usage, const android::Rect& bounds, void** vaddr);

    status_t _ZN7android19GraphicBufferMapper4lockEPK13native_handlejRKNS_4RectEPPv(
            buffer_handle_t handle, uint32_t usage, const Rect& bounds, void** vaddr) {
        return _ZN7android19GraphicBufferMapper4lockEPK13native_handlejRKNS_4RectEPPv(
                handle, (int)usage, bounds, vaddr);
    }

    /* status_t GraphicBufferAllocator::alloc */
    status_t __attribute__((weak)) _ZN7android22GraphicBufferAllocator5allocEjjiiPPK13native_handlePi(
            uint32_t width, uint32_t height, PixelFormat format, int usage,
            buffer_handle_t* handle, uint32_t* stride);

    status_t _ZN7android22GraphicBufferAllocator5allocEjjijPPK13native_handlePj(
                uint32_t width, uint32_t height, PixelFormat format, uint32_t usage,
                buffer_handle_t* handle, uint32_t* stride) {
        return _ZN7android22GraphicBufferAllocator5allocEjjiiPPK13native_handlePi(
                width, height, format, (int)usage, handle, stride);
    }

    status_t __attribute__((weak)) _ZN7android5Fence4waitEj (unsigned int timeout);

    status_t _ZN7android5Fence4waitEi (int timeout) {
        return _ZN7android5Fence4waitEj((unsigned int)(timeout));
    }
}

// ---------------------------------------------------------------------------

}; // namespace android
