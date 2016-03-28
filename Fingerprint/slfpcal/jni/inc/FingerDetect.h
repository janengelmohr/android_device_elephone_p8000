#ifndef FINGER_DETECT_H
#define FINGER_DETECT_H
#include "FFT96.h"
#define FFT_SIZE      32
//#define PI            3.1415926535
#define FFT_LINE_NUM  2
#define		Eps				0.000000001f


void myFFT2D(float *fData, compx* scFFT, int BufLen);
void FingerPrintValidationFFT2BLK(unsigned char* img, int width, int height, int* pResult);
void myFFT2D48(float *fData, compx* scFFT, int BufLen);
int FingerDetectBLK(unsigned char* img,int h,int w,int ifftMin);
int FingerDetectBLKStrip(unsigned char* img,int h,int w,int ifftMin);

// for strip shape sensor
void FingerPrintValidationFFT2BLKStrip(unsigned char* img, int width, int height, int* pResult);
void DeleteLowFrequency(compx* scFFT, float* pfBuf, int BufLen, int iResultLen);
void FFTShift(compx* scFFT,int BufLen);
#endif





