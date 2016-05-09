#ifndef __SL_FPSVC_HAL__
#define __SL_FPSVC_HAL__
#include "metatypes.h"

void FPSVCRX_UpdateConfig(
	int  in_org_h,
	int  in_org_w,
	int* palg_frame_h,
	int* palg_frame_w,
	int* palg_frame_ppi
	);

void FPSVCRX_FingerStateReset();

Int32 FPSVCRX_FrameGet(
	UInt8	*pBuffer, 
	UInt32	frameCount, 
	Int32	iWidth,
	Int32 	iHeight,
	Int32*	pfingerstate
	);

void FPSVCRX_FingerStateReset();

void FPSVCRX_GetSleepParam();

void FPSVCRX_SetSleepParam();

void FPSVCRX_SetWakeUpParam();

#endif
