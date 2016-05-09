#ifndef __SL_CHIPBINDER_H__
#define __SL_CHIPBINDER_H__
typedef signed int (*slf_read_f)(unsigned int addr, unsigned int *pvalue);
typedef signed int (*slf_write_f)(unsigned int addr, unsigned int value);

int SLChipe_crypt(slf_read_f slf_read, slf_write_f slf_write);
#endif
