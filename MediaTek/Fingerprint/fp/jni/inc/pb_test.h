#ifndef __PB_TEST_H__
#define __PB_TEST_H__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/time.h>
#include <assert.h>

#include "bmp.h"

void Initail();
void enroll(int pep, int fin, int *flg);
void verify(float *farVer, float *frrVer);
//void enroll(char* imagePath, int enrollNum,int people);
//void verify(char* imagePath,int totalTemplate,int verifyNum,int enrollNum);

#endif
