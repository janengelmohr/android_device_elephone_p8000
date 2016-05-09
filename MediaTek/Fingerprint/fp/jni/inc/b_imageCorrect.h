#ifndef B_IMAGECORRECT_H_
#define B_IMAGECORRECT_H_

#define  IMAGE_H    110
#define  IMAGE_W    118

#ifndef PI
#define PI			3.1415926
#endif

#ifndef EPI
#define EPI			57.29578
#endif
// src: input and output
//s=1
// input need image-insert or cut the white lines

void b_imageCorrect(unsigned char* src);

void b_imageInsert(unsigned char* src, int s);


#endif
