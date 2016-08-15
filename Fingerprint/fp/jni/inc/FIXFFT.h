//#include <math.h>
#include "SL_Math.h"
#ifndef PI
#define PI 3.14159265358979
#endif
#define FFT_PRECISION 7
#define SIZE_POINT  32
#if SIZE_POINT == 32
	#define TWO_LEN 5
    #define DIV_COMMEND  2
#elif SIZE_POINT == 16
	#define TWO_LEN 4
	#define DIV_COMMEND  3
#endif

//#define bool int

struct complex_int
{
	int real;
	int img;
};

int log_two_length(int m);
void init_data(struct complex_int* in);
void init_wn(struct complex_int *wn, int flag);
void change_pos_xn(struct complex_int* input);
void add(struct complex_int a, struct complex_int b, struct complex_int *c);
void sub(struct complex_int a, struct complex_int b, struct complex_int *c);
void mul(struct complex_int a, struct complex_int b, struct complex_int *c);
void comput_butterflies(struct complex_int* in_out_put, struct complex_int* wn, int beg, int c_num, int k, int i);
void fft(struct complex_int* in_out_put);
void ifft(struct complex_int* in_out_put);

void fft2d(struct complex_int* in_out_put);
void ifft2d(struct complex_int* in_out_put);
