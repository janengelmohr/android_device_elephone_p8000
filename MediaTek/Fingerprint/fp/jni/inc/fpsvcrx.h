/*  ====================================================================================================
**
**  ---------------------------------------------------------------------------------------------------
**
**	File Name:  	fpsvcrx.h
**	
**	Description:	
**
**					this is kernal code of SW framework.
**					It contributes one of functionalities of SW Platform. 
**					If the checkin is CR not PR, to add change History to this file head part 
**					will be appreciated.
**
**  ---------------------------------------------------------------------------------------------------
**
**  Author:			Warren Zhao
**
** -------------------------------------------------------------------------
**
**	Change History:
**	
**	Initial revision
**
**====================================================================================================*/
#ifndef __FPSVC_RX_HEADER__
#define __FPSVC_RX_HEADER__

#ifdef __cplusplus
extern "C" {
#endif

typedef int   (*CB_FPSVCRX_FPDetectError_Func_t)(char* inErrorStr);
typedef int   (*CB_FPSVCRX_IsIdleState_Func_t)(void);
typedef int   (*CB_FPSVCRX_FingerDown_Func_t)(void);
typedef int   (*CB_FPSVCRX_FingerUp_Func_t)(void);
typedef char* (*CB_FPSVCRX_GetFrmBuff_Func_t)(void);
typedef int   (*CB_FPSVCRX_VerifyReq_Func_t)(void);
typedef int   (*CB_FPSVCRX_VerifyLaunchReq_Func_t)(void); 	//add by matthew
typedef int   (*CB_FPSVCRX_CheckClientDeathTiming_Func_t)(void);

void FPSVCRX_init(
	int*	alg_frame_w,
	int*	alg_frame_h,
	int*	alg_frame_ppi,
	CB_FPSVCRX_FPDetectError_Func_t				inFPDetectErrorFunc,
	CB_FPSVCRX_FingerDown_Func_t				inFingerDownFunc,
	CB_FPSVCRX_FingerUp_Func_t					inFingerUpFunc,
	CB_FPSVCRX_GetFrmBuff_Func_t				inGetFrmBuff,
	CB_FPSVCRX_VerifyReq_Func_t					inVerifyReqFunc,
	CB_FPSVCRX_VerifyLaunchReq_Func_t			inVerifyLaunchReqFunc,
	CB_FPSVCRX_CheckClientDeathTiming_Func_t	inCheckTimingFunc
	);

void FPSVCRX_SetIdleState(
	int		inIsIdle
	);

void FPSVCRX_SetScreenState(
	int		inIsScreenOn
	);
int FPSVCRX_SetWakeUpState(
	int WakeUpState
	);

int FPSVCRX_SetVirtualKeyState(
	int VirtualKeyState
	);

int FPSVCRX_SetFingerPrintState(
	int FingerPrintState
	);

int FPSVCRX_SetVirtualKeyCode(
	int VirtualKeyCode
);

//add by matthew
int FPSVCRX_SetFunctionKeyState(
	int FunctionKeyState
);

int FPSVCRX_WakeUpScreen(void);

int FPSVCRX_PostFunctionKey(
	int g_fingerid, 
	int enable, 
	int state
	);

void FPSVCRX_GetDefaultParam(
	int* virtualkeystate,
	int* wakeupstate,
	int* fingerprintstate,
	int* virtualkeycode
	);

#ifdef __cplusplus
}
#endif

#endif
