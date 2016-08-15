#ifndef __MATCH_FINE_TUNE_H__
#define __MATCH_FINE_TUNE_H__

#include "ForDefineNew.h"
#include "ForDefine.h"

int FineTune(
		unsigned char *img, 
		sSizeInfo sImgSizeInfo,
		unsigned int* iTuneDAC, 
		unsigned int* iTuneAG20, 
		unsigned int* iTuneAG2C, 
		int iFingerDetectBlkNum, 
		int iDCMode, 
		int iShapeMode
		);

void GetSleepInfo(
		unsigned int* iDAC, 
		unsigned int* iThreshold, 
		unsigned int* iTuneAG20, 
		unsigned int* iTuneAG2C
		);

void	InitisFisrtSetSleepInfo();

void SetFineTuneCoeff(int* Coeff);

#endif
