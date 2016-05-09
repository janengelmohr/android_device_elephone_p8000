#ifndef _FFT96_H_
#define _FFT96_H_
//#include <iostream>
#include<math.h>

//#define		PI		3.14159f//26535897932384626433832795028841971
#define		LEN96	96
#define		LEN32	32
typedef struct
{
	float real;
	float imag;
}compx;
#ifdef __cplusplus
extern "C"{
#endif
compx CalcSCMul(compx a,compx b);
void FFT(compx *xin, int iFFT_N, float *SIN_TAB);
void cutsize96(float* fData, int iWidth, int iHeight);
void FFT2D96(float *fData, compx* scFFT);
void iFFT1D96(compx *xin, compx *scTemp96);
void iFFT2D96(compx *scData, compx* scFFT);
void FFT1D96(compx *xin, compx *scTemp96);
void iFFT(compx *xin, int iFFT_N, float *SIN_TAB);
void Conj(compx* a, int N);
float Norm(compx *a, int N);
void scAbs(compx *a, int N, float *b);
void DivC(compx* a, int N, float C);
void FFT1D48(compx *xin, compx *scTemp48);
#ifdef __cplusplus
}
#endif
#endif
