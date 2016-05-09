

#ifndef __HUAWEISESSION_H__
#define __HUAWEISESSION_H__

#ifdef __cplusplus
extern "C" {
#endif


#include <stdint.h>

typedef struct {
	int16_t width; /** < width in pixels, -1 indicates unknown value*/
	int16_t height; /** < heigt in pixels, -1 indicates unknown value*/
	int16_t ppi; /** < sampling intensity, -1 indicates unknown value*/
	int8_t bits_per_pixels; /** < Greyscale bit depth of the pixels, -1 indicates unknown value */
	int8_t channels; /** < Number of channels */
	int8_t greyscale_polarity; /** < 0 indicates that ridges are black, valleys are white, 1 indicates the inverse situation , -1 indicates unknown value*/
} huawei_frame_format_t;

typedef struct {
	huawei_frame_format_t format;
	uint16_t frame_count;
	uint8_t* buffer;
	uint32_t capacity;
} huawei_image_t;

struct _HuaweiEnrolSession{
	int16_t nMaxNum;	//最大模板数目
	int16_t nUsedNum;   //已使用数目
	uint8_t* pDataBuf;	//缓存空间
};

struct _huawei_template_t{
	uint8_t feat[1];  //由算法决定长度
};

typedef struct _HuaweiEnrolSession enrolment_session_t; //录入会话，实际数据结构由算法库内部定义
typedef struct _huawei_template_t huawei_template_t; //指纹模板，实际数据结构由算法库内部定义

typedef struct huawei_image_quality_t {
	int coverage; //指纹图像覆盖区域（单位为：像素数目）
	int quality; //图片质量, [0~100]
} huawei_image_quality_t;

typedef struct ialgorithm_t {
	// 开始指纹录入，返回会话指针
	enrolment_session_t* (*enrolStart)(); //重要，接口必须要实现
	// 向录入会话添加指纹图像，图像来自指纹传感器，由于需要多个指纹图像，返回值标志当前进度（0-100）
	int32_t (*enrolAddImage)(enrolment_session_t *session,
		huawei_image_t* image,
		huawei_image_quality_t* image_quality); //重要，接口必须要实现
	// 当录入会话中添加指纹图像进度达到100%时调用该接口提取指纹模板数据

	int32_t (*enrolGetTemplate)(enrolment_session_t *session,	//会话接口
								huawei_template_t** tpl);			//指纹模块，指针的指针
	// 结束录入会话
	int32_t (*enrolFinish)(enrolment_session_t *session); //重要，接口必须要实现

	// 指纹认证。返回值<0,失败；result表示认证成功后返回的指纹模板列表（candidates）中元素的索引值。Score表示图片匹配度。
	int32_t (*identifyImage)(huawei_image_t* img,
		huawei_template_t** candidates,		//外部输入指针数组
		uint32_t candidate_count,
		int32_t* result,					//result正常取值范围 [ 0,candidate_count-1 ], -1：无结果（异常)
		int32_t* score,						//score取值范围 [ 0,100 ]
		huawei_image_quality_t* image_quality,
		int32_t* update); //算法内部是否更新了模板，>0：有更新

	// 指纹模板数据的压缩和解压，用于本地保存。
	uint32_t (*templateGetPackedSize)(huawei_template_t *tpl);
	int32_t (*templatePack)(huawei_template_t* tpl, uint8_t* dst);
	int32_t (*templateUnPack)(uint8_t* src, uint32_t length, huawei_template_t** tpl);

	// 指纹模板数据资源删除
	void (*templateDelete)(huawei_template_t* tpl); //重要，接口必须要实现
} ialgorithm_t;

// 获取指纹算法函数指针
extern void getIalgorithm(ialgorithm_t* ialgorithm);

// 获取指纹算法库版本
extern int32_t getIalgorithmVersion(void);  //4字节的字符串，例如"1062"

extern int32_t enrolSet(int16_t enrolNum,int16_t mt_size);

#ifdef __cplusplus
}
#endif

#endif	// __HUAWEISESSION_H__
