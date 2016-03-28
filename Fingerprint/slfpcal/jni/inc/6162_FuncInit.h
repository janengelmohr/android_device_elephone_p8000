#ifndef FUNCTION_INITIAL_6162_H
#define FUNCTION_INITIAL_6162_H
typedef  unsigned int UINT ;
#include "6162_AdjustImage.h"
#include "FingerDetect6162.h"
#include "ForDefine.h"
#include "paser_config.h"
int FuncInitial_6162(unsigned char* IMGBUF, sParam sParamToInIt, sSizeInfo sImgSizeInfo, 
				int iMTOn, UINT* iCurrent0c, UINT* iCurrentb8);

void ChangeGear_6162(int bAdjustImageState,int bFingerDetectState);

void RedoFuncInit_6162(int flag);

UINT GetBestB8Value_6162();

int ReturnAdState_6162();

int ReturnFdState_6162();

void SetGainFlag_6162(int flag);

void GetGear_6162(int gear);

void JudgeAndSetConfig_6162();

void InitIsFirst_6162();

int GetiFFTMeasure_6162();

int GetiCovMeasure_6162();

void InitPhase_1_Process_6162(unsigned char* IMG, int bright, int dark, int mean, int var);

void InitPhase_1_ChangeGear_6162(int iAGResult);

void InitPhase_2_Process_6162(unsigned char* IMG ,int ifftMin,int bright,int dark,int mean,int var);

void MatchTune_6162(unsigned char* IMG ,int ifftMin,int bright,int dark,int mean,int var);

void InitMatchTune_6162();
void FindBestParam_6162();
void FindMinMax_6162(UINT iCurrent0c, UINT* pMax, UINT* pMin);
void GetParamMatchTune_6162(UINT* i0c, UINT* ib8, UINT* i0cDown, UINT* i0cUp);
void CutForProcess_6162(unsigned char* IMGBUF, sSizeInfo sImgSizeInfo);


#endif
