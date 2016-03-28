#ifndef  _FINGERDETECT6162_H_
#define  _FINGERDETECT6162_H_

#include "FFT96.h"
#define FFT_SIZE      32
#define FFT_LINE_NUM  2
#define	Eps			0.000000001f

//FingerDetectBLK(): 12~15ms
/*
void myFFT2D(float *fData, compx* scFFT, int BufLen);
void FingerPrintValidationFFT2BLK(unsigned char* img, int width, int height, int* pResult);
void myFFT2D48(float *fData, compx* scFFT, int BufLen);

void FingerPrintValidationFFT2BLKStrip(unsigned char* img, int width, int height, int* pResult);
void DeleteLowFrequency(compx* scFFT, float* pfBuf, int BufLen, int iResultLen);
void FFTShift(compx* scFFT,int BufLen);
void CheckCorner(unsigned char* img, int width, int height, int* pResult);
float CalcSNR(unsigned char* img, int height, int width);
*/

void DeleteLowFrequency(compx* scFFT, float* pfBuf, int BufLen, int iResultLen);
int FingerDetectBLK(unsigned char* img,int h,int w);
int FingerDetectBLKStrip(unsigned char* img,int h,int w);
void FingerBLKTest(unsigned char* img, int width, int height, float* pResult);
void ImFilterf(unsigned char *pImageSrc, int iWidth, int iHeight, float *pTemp);


#endif
