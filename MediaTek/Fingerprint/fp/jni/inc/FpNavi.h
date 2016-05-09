
#ifndef __FPNAVI__HEADER__
#define __FPNAVI__HEADER__

#include <stdio.h>
#include <stdlib.h>
#include "ForDefine.h"
#include "FFT96.h"


/*Fingerprint navigation api,pfBefore gives Last frame,pfAfter gives current frame,iImgHeight and iImgWidth 
* give height and width of image, fOrientation gives the clockwise orientation(0~360 degree) of image shift,
* and fDistance gives the offset distance in shift orientation.
*/
void OrientationCalc(unsigned char* pfBefore, unsigned char* pfAfter, int iImgWidth, 
					 int iImgHeight, int* fOrientation, int* fDistance);

void UTQCalc(unsigned char* pfBefore, unsigned char* pfAfter, int lWidth, int lHeight, 
			 int* aiDX, int* aiDY, float* afMax);
void AngleCalc(int iDx, int iDy, int iImgWidth, int iImgHeight, int* fAngle, int* fDistance);

#endif
