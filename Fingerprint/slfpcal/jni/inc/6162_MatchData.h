#ifndef MATCH_DATA_6162_H
#define MATCH_DATA_6162_H
typedef  unsigned int UINT ;
int MatchData_6162(unsigned char *img, int height, int width, 
			  int ifftMin,int bright,int dark,int mean, int var, 
			  UINT i0c, UINT ib8, UINT i0cDown, UINT i0cUp, int iMatchTuneOn, unsigned int* iTune0C);
void FindBoundary_6162();
void MatchData_Process_6162(int iMean);
void Change0C_6162();
int GetPhase_6162(int iMean);
void GetDelta_6162(int iMean, int iPhase);
// void GetAGAndMean(unsigned char *img, int height, int width, 
// 				  int bright, int dark, int mean, int var, int* iAGResult, int* iMean);


#endif
