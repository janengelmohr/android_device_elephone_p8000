#ifndef  __PASER_CONFIG_H__
#define  __PASER_CONFIG_H__
#include <unistd.h>

typedef struct configs{
    unsigned int addr_32;
    unsigned int val_32;
}config_data;

/*
typedef struct
{
	int iWidth;
	int iHeight;
	int iUp;
	int iDown;
	int iLeft;
	int iRight;
} sSizeInfo ;

typedef struct
{
	int ifftMin;
	int icovMin;
	int bright;
	int dark;
	int mean;
	int var;
}sParam;
*/
typedef struct param{
	unsigned int version;
	unsigned int i0cFixedM;
	unsigned int ib8FixedM;
	unsigned int i0cMatchTuneDownM;
	unsigned int i0cMatchTuneUpM;
} _Param;

#ifdef __cplusplus
extern "C"{
#endif
unsigned int get_param(struct param *param, const char* path);
unsigned int set_param(struct param *param, const char* path);
unsigned int  set_configs(struct configs config[], const char* path, unsigned int count, const char *mode);
unsigned int  get_configs(struct configs config[], const char* path,unsigned int count, const char *mode);
int set_node_value(const char * file_path,const char *path, const char *val);
int get_node_value(const char * file_path, const char *path, char *out_val, size_t out_val_len);
int get_config_length(const char *mode, const char* path);
int get_chipid_string(char* spath, char** str);
#ifdef __cplusplus
}
#endif
#endif
