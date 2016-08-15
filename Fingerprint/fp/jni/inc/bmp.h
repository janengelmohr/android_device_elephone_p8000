#ifndef __RW_Image_H__
#define __RW_Image_H__

#define W1			256
#define H1			256
#define W2			118
#define H2			110

#define  MAX_REG_NUM         30
//#define  MAX_TEMPLATE_NUM    10
#define  MEMORY_LENGTH       4*1024*1024
//判断文件是否存在
// <0  不存在, >=0 存在
#define R_OK 4 /* Test for read permission. */　　
#define W_OK 2 /* Test for write permission. */
#define X_OK 1 /* Test for execute permission. */　
#define F_OK 0 /* Test for existence. */ 

#define  _LOG_OUT_        1
#define  _LOG_CURVE_      1
#define  _DEL_FAIL_TEMPL  1
#define  S_FAILED         -1
#define  S_BREAK          2
#define  S_SUCCESS        0
#define  S_CONTINUE       1

typedef enum FRR_FAR
{
	_FRR_ ,
		_FAR_
};

enum _IMAGE_POSITION{
	BELOW ,
		ABOVE 
};

#define BI_RGB              0L
#define BI_RLE8             1L
#define BI_RLE4             2L
#define BMP_TYPE            0x4d42
#define WIDTHBYTES(bits)    (((bits)+31)/32*4)



#pragma   pack(1)  // 让编译器按1字节对齐


typedef struct _BITMAPFILEHEADER_
{

	unsigned short		bfType;
	unsigned long		bfSize;
	unsigned short		bfReserved1;
	unsigned short		bfReserved2;
	unsigned long		bfOffBits;

} WL_BITMAPFILEHEADER, *LPWL_BITMAPFILEHEADER;

typedef struct _BITMAPINFOHEADER_
{

	unsigned long		biSize;
	long				biWidth;
	long				biHeight;
	unsigned short      biPlanes;
	unsigned short      biBitCount;
	unsigned long		biCompression;
	unsigned long		biSizeImage;
	long				biXPelsPerMeter;
	long				biYPelsPerMeter;
	unsigned long		biClrUsed;
	unsigned long		biClrImportant;

} WL_BITMAPINFOHEADER, *LPWL_BITMAPINFOHEADER;

typedef struct _RGBQUAD_
{

	unsigned char    rgbBlue;
	unsigned char    rgbGreen;
	unsigned char    rgbRed;
	unsigned char    rgbReserved;

} WL_RGBQUAD, *LPWL_RGBQUAD;


#pragma   pack()  // 还原


#endif //结束第一个#ifndef


#ifdef __cplusplus
extern "C" {
#endif


int GFPLoadImage(const char filename[], unsigned char *ucImage, int *h, int *w);
void GFP_Write_Bmp_1(const char filename[], unsigned char *ucImg, int h, int w);
void GFP_Write_Bmp_8(const char filename[], unsigned char *ucImg, int h, int w);
void GFP_Write_Bmp_24(const char filename[], WL_RGBQUAD *ucImg, int h, int w);

void SaveWSQ(char *name, unsigned char *data, int len);
int WritePGM(char *PGMFileName, unsigned char *idata, int height, int width);
double ImageComparePSNR(unsigned char *idata1, int height1, int width1, 
						unsigned char *idata2, int height2, int width2);
int LoadBmp(const char filename[], unsigned char *ucImage, int *h, int *w);
int WriteBmp(const char filename[], unsigned char *ucImage, int h, int w);
int GFP_Bmp_Change_8(unsigned char *image,int imwith_t,int imhight_t,int *h, int *w);

#ifdef __cplusplus
}
#endif


