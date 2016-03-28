#ifndef ADJUSTIMAGE_6162_H
#define ADJUSTIMAGE_6162_H
typedef  unsigned int UINT ;
//#include <iostream>


// img: ͼ������
// height width: ͼ���иߡ��п�
// bright dark: ����ϵ��


int		adjustImage_6162(unsigned char *img, int height, int width, int bright, int dark,int mean,int var,int B8Fixed);
int		FingerImageAdjust_6162(unsigned char *img,int height, int width,int bright,int dark,
					   int mean,int var,int IsUpload, int B8Fixed, UINT* i0c, UINT* ib8);
void	SetConfig_6162(int i,int flag, UINT* i0c);
int		RegCompare_6162(int bAdjustImageState, UINT i0c);
int		GetB8Measure_6162();
int		GetB8Value_6162();
void	WriteBestB8_6162(UINT iBestB8Val);


void	SetBoundary0C_6162(int iAGResult, UINT* i0c);
void	SetMiddle0C_6162(UINT Last0C, int iAGResult, UINT* i0c);
int		JudgeIsOutOfGear_6162(UINT i0cFixed, UINT iMTMaxStep);
void GetAdjustValue_6162(unsigned char *img, int height, int width,int* piResult, int* piMeanAll, 
					int* piDarkAll, int* piBrightAll, int* piMean64, int* piDark64, int* piBright64);
float DarkCount_6162(unsigned char *img, int height, int width, int iThresh);
void BlkHistogram_6162(unsigned char *img, int height, int width, int* pHis);
void SortIndex_6162(float * fValue, int * iIndex, int len);
int GetOrder_6162(unsigned char *img, int height, int width, float* fOriValue);
void GetAGAndMean_6162(unsigned char *img, int height, int width, int bright, int dark, int mean, int var, int* iAGResult, int* iMean);


// for strip shape sensor

#endif
