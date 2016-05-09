#ifndef FINGER_DETECT_OLD_H
#define FINGER_DETECT_OLD_H

typedef struct
{
	float real;
	float imag;
}compx;

#ifdef __cplusplus
extern "C"{
#endif
void myFFT2DOld(float *fData,compx* scFFT, int BufLen);
void DeleteLowFrequencyOld(compx* scFFT, float* pfBuf, int BufLen, int iResultLen);
void FFTShiftOld(compx* scFFT,int BufLen);
#ifdef __cplusplus
}
#endif
#endif
