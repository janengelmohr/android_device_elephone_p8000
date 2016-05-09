/*
 * author:liqi
 * Port:CheckImage() ;
 * return value: 1(successful) or 0(failed)
 *
 * b_insert : 0.13ms
 * b_correct : 32.32ms
 * ImPreProcess(1<= iFingerCount<= FINGERSTATECOUNT) : 0.63ms
 * ImPreProcess(iFingerCount== FINGERSTATECOUNT) : 0.88ms
 */

#ifndef	IMPREPROCESS_H
#define	IMPREPROCESS_H


#ifdef __cplusplus
extern "C"
{
#endif
//void ImPreProcess(unsigned char	*pImageSrc, int iWidth, int iHeight, int iFingerCount);

//void ImFilter(unsigned char	*pImageSrc, int iWidth, int iHeight, unsigned int *pTemp);

int CheckImage(unsigned char *pImageSrc, int iWidth, int iHeight, int* fingerstate);
void CheckImage_Reset();
void ProcSetConfig(
		int FingerDetectThreshold,
		int FingerDetectFrame,
		int FingerReleaseFrame
		);

#ifdef __cplusplus
}
#endif
#endif
