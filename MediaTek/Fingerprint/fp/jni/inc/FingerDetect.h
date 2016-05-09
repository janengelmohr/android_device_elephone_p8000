#ifndef FINGER_DETECT_H
#define FINGER_DETECT_H

//#include "SL_Complex.h"
#include "FIXFFT.h"
#include <string.h>
#define NO_HUAWEI_TRUSTZONE

#ifdef NO_HUAWEI_TRUSTZONE
	#include <stdlib.h>
#else
	extern void *malloc(unsigned int num_bytes);
	extern void free(void *mem);
#endif




void	myFFT2D(int *fData, struct complex_int* scFFT, int BufLen);
int		FingerDetectBLK(unsigned char* img,int h,int w,int ifftMin);\
void	DeleteLowFrequency(struct complex_int* scFFT, int* pfBuf, int BufLen, int iResultLen);
void	FFTShift(struct complex_int* scFFT,int BufLen);
void	FingerBLKTest(unsigned char* img, int width, int height, int* pResult);
#endif





