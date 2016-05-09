#ifndef _FILESAVE_H_
#define _FILESAVE_H_

#include <fcntl.h> 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <time.h>

#define MODE_F					1       	//fopen fwrite fread
#define MODE_O					0       	//open write read


//操作返回值
#define ERR_OK					0		//操作成功
#define ERR_INIT_NODEVICE		-1		//设备初始化失败/无设备
#define ERR_OVERTIME			-2		//操作超时
#define ERR_MATCH				-3		//比对失败
#define ERR_OPEN_FILE			-5		//打开文件失败
#define ERR_WRITE_FILE			-6		//写文件失败
#define ERR_READ_FILE			-7		//读文件失败
#define	ERR_NULL				-8		//空指针
#define ERR_PARAMETER			-9		//参数错误
#define ERR_NODEFINE			-255	//未定义错误

#define FPALG_IMG_SIZE			(256*256)		//手指图像大小
#define FPALG_MB_SIZE			(1600000)	//每个手指对应的特征
#define FPALG_MAX_MB_NUM		5				//最多录入5个手指			
#define FPALG_MAX_TZ_NUM		8				//每个手指最多3个特征
#define FPALG_FP_NUM  			30				//单个手指最多采集的次数
#define FP_WIDTH 				118				//指纹图像数据W
#define FP_HEIGHT				110				//指纹图像数据H
#define FP_MATCH_TMEOUT			5				//比对超时时间


#define FP_THRESHOLD_TIMES		15


#pragma pack(push) //保存对齐状态
#pragma pack(1)

//单枚指纹数据结构体
typedef struct{
	int flag;	//表示是否闲置
	int num;	//表示对应的模板数
	unsigned char cpFpImg[FPALG_MAX_TZ_NUM][FPALG_IMG_SIZE];//指纹图像数据
	unsigned char cpFpTemp[FPALG_MB_SIZE];//指纹特征数据
}StuFpInfo; 

#pragma pack(pop)

int Sys_fWrite(int imode, char *fpath, int ioffset, char *fpDatas, int ilen);
int Sys_fRead(int imode, char *cpath, int ioffset, char *cpDatas, int ilen);
int Sys_fLenth(int iMode, char * cpath);


int SaveBMP(char* file,unsigned char* pImage,int X,int Y);
//int SF_WriteFpInfo(int index,int num,unsigned char **cpImage,unsigned char *cpTemp);
//int SF_ReadFpInfo(int mode);

#endif 


