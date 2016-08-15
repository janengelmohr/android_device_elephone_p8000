#ifndef SL_MATH_H
#define SL_MATH_H

#define	SL_PI			(float)3.1415926
#define SL_2PI			(float)6.2831852
#define SL_PI_DIV_2		(float)1.570796

#define  ABS(x)  (((x) >= 0) ? (x) : -(x))
#define  GetMin(a,b,c)  ((a>b?b:a)>c ? c:(a>b?b:a))

int		SL_int_abs(int x);

void	SL_uchar_memcpy(unsigned char* dst, unsigned char* src, int iLength);

float	SL_Sqrt(float x);
int		SL_int_Sqrt(int x);

float	SL_Sin(float x);
float	SL_Cos(float x);

float	SL_Floor(float x);

#endif