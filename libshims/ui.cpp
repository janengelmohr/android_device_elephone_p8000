#include <ui/Rect.h>
#include <ui/Region.h>
#include <ui/PixelFormat.h>
#include <ui/GraphicBuffer.h>
#include <ui/GraphicBufferMapper.h>

namespace android {

const Rect Rect::INVALID_RECT{0, 0, -1, -1};

const Region Region::INVALID_REGION(Rect::INVALID_RECT);

bool GraphicBuffer::needsReallocation(uint32_t inWidth, uint32_t inHeight,
        PixelFormat inFormat, uint32_t inUsage)
{
    if (static_cast<int>(inWidth) != width) return true;
    if (static_cast<int>(inHeight) != height) return true;
    if (inFormat != format) return true;
    if ((static_cast<uint32_t>(usage) & inUsage) != inUsage) return true;
    return false;
}

}
