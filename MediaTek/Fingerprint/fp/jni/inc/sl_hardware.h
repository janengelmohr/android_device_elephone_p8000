#ifndef __SL_HARDWARE_HEADER_H__
#define __SL_HARDWARE_HEADER_H__

#include "metatypes.h"
#include "sl_fphwopmode.h"

#define	ARRAY_SIZE(a)		(sizeof(a) / sizeof( (a)[0] ) )

typedef struct {
  UInt32     addr;
  UInt32     value;
} SL_SENSOR_CONFIG, *PSL_SENSOR_CONFIG;

#define FP_SENSOR_DEVICE		"/dev/silead_fp_dev"

#define SPI_BITS			8
#define SPI_SPEED			(9 * 1000 * 1000)
#define SPI_DELAY			1

#define SPI_RD				0x00
#define SPI_WR				0xFF

//#define ARRAY_SIZE(x)			(sizeof(x) / sizeof(x[0]))

#define SL_FRAME_PAGE_SIZE		128

struct _SPI_RX_BUFFER {
    UInt8		rx_head[2];
    UInt8		page_buff[0];
} __attribute__ ((packed));

typedef struct _SPI_RX_BUFFER  SPI_RX_BUFFER, *PSPI_RX_BUFFER;

#ifdef __cplusplus
extern "C" {
#endif

//only called by fpsvcdrx & fpcal.
int SLFP_HWInit(void);
//only called by fpsvcdrx & fpcal.
void SLFP_HWDeinit(void);
//only called by fpsvcdrx & fpcal.
void SLFP_HWReset(void);

Int32 SLFP_HWRegisterGet(UInt32  addr, UInt32 *pValue);

Int32 SLFP_HWRegisterSet(UInt32 addr, UInt32 value);

Int32 SLFP_HWFrameGet(
	UInt8	*pBuffer, 
	UInt32	frameCount, 
	Int32	iWidth,
	Int32 	iHeight
	);

int SLFP_DetectFingerPress(void);

void SLFP_HW_SysParam(
	void* in_pconfig
	);

int SLFP_HWHandleOpen();

int SLFP_HWSysParamLoad();

UInt32 SLFP_HWGetChipID();

#ifdef __cplusplus
}
#endif
#endif


