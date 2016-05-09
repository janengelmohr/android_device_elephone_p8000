#ifndef _SLALGADPATER_API_H_
#define _SLALGADPATER_API_H_

//#define SL_MAX_ENROLL_COUNT				8
//#define SL_MIN_ENROLL_COUNT				3

#define			SL_ALG_SUCCESS				0
#define			SL_ALG_CHECK_IMG_FAIL		-1
#define			SL_ALG_ENROLL_FAIL			-5



typedef int         (*FingerDetectFunc)(void* frame);

void SLALGADPATER_Init(int image_h,int image_w,int max_enrolls,int max_templates,int auto_learning);

void SLALGADPATER_DeInit(void);
/*
return: -1= img is not qualified;
        0 = enroll failure;
        1 = enroll is OK; 
		100=enroll finished;
*/
int SLALGADPATER_Enroll(
	void*				fpFrame,	// [INPUT]: the fingerprint frame
	int					fpTemplateImgBuffSize,	// [INPUT]: the input template buffer size
	unsigned char*		ucImageFeature,			//
	int					ucImageFeatureSize,		//
	unsigned char 		**ppFpTemplateImage,
	unsigned char		*FpAlgTempImg,
	int					fingerIndex,
	int*				percent,
	int*				enroll_area,
	int*				opRetNum				//???? what this is for? what kind of interface design. 
	);


/*
return: -1= img is not qualified;
        0 = verification failure;
        1 = verification is OK; 
*/
int SLALGADPATER_Verify(
 	void*				fpFrame,
	int					fpTemplateImgBuffSize,
	unsigned char*		ucImageFeature,
	int					ucImageFeatureSize,
	int					fingerIndex
	);

/*
 * Name:Silead_GetValidImage
 * para:unsigned char pImageSrc,copy_or_not
 *return 0 ? 1
 * */
int SLALGADPATER_CheckImageValid(
	unsigned char	*pImageSrc
	);

int SLALGADPATER_VerifyExpand(
	unsigned char	**ucTemplateImage, 
	int				n, 
	unsigned char	*ucImageFeature, 
	unsigned char	*ucImage, 
	int			*fSimilarity
	);
void SLALGADPATER_Reset();
void SLALGCheckImage_Reset();


void SLALGADPATER_UnPack(int index,unsigned char *pTemplate);

#endif
