#ifndef __MATHCALCULATION__HEADER__
#define __MATHCALCULATION__HEADER__
#include <math.h>

void ImFilter(const float* pfBig, const int iBigWidth, const int iBigHeight, 
			  const float* pfKernel, const int iKWidth, const int iKHeight, float* pfResult);
void ImPadding(const float* pfImg, const int iImgWidth, const int iImgHeight, 
			   const int iPaddingSize, float* pfBig, int iBigWidth, int iBigHeight);
void ImMax( float* pfImg,  int iImgWidth,  int iImgHeight, 
	int *iIndexX, int *iIndexY, float *fMax);

#endif

