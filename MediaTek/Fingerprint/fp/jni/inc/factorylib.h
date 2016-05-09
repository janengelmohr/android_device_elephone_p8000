#ifndef __FACTORY_TOOL__
#define __FACTORY_TOOL__
#ifdef __cplusplus
extern "C"{
#endif

typedef void (*SLFACT_INF_IND_FUNCT_t)(char *opInfo);

typedef struct {
	int unused;
} SLFactoryTestInfo_t;

#define DEADPIXEL_LEVEL1  1
#define DEADPIXEL_LEVEL2  2
#define DEADPIXEL_LEVEL3  3
#define DEADPIXEL_LEVEL4  4
int spi_test(int* id);
int finger_detect(unsigned char *FpImg, int w, int h);
int bubble_test(unsigned char *FpImg,int w, int h);
int deadpixel_test(unsigned char *FpImgPrevious, unsigned char *FpImgAfter,int w, int h, int w_strip, int h_strip,SLFACT_INF_IND_FUNCT_t inInfoIndFct);
#ifdef __cplusplus
}
#endif
#endif
