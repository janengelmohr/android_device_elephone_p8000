#ifndef SILEAD_BASE_TYPE_H
#define SILEAD_BASE_TYPE_H

#include <linux/types.h>

#define ANDROID_LOG_INFO "fpcal" 
#define __android_log_print(i, m, f,...)	printf("\n%s:%s",i,m); printf(f,##__VA_ARGS__);

#define SPI_CPHA			0x01
#define SPI_CPOL			0x02
#define SPI_MODE_0			(0|0)
#define SPI_MODE_1			(0|SPI_CPHA)
#define SPI_MODE_2			(SPI_CPOL|0)
#define SPI_MODE_3			(SPI_CPOL|SPI_CPHA)

#define SPI_CS_HIGH			0x04
#define SPI_LSB_FIRST		0x08
#define SPI_3WIRE			0x10
#define SPI_LOOP			0x20
#define SPI_NO_CS			0x40
#define SPI_READY			0x80
#define SPI_IOC_MAGIC		'k'

#define Pages 1
#define SPI_RD  0x00
#define SPI_WR  0xff
#define LEN (14*1024)


#define SILEAD_TRUE			1
#define SILEAD_FALSE		0


#define SPI_MSGSIZE(N) ((((N)*(sizeof (struct spi_ioc_transfer))) < (1 << _IOC_SIZEBITS)) ? ((N)*(sizeof (struct spi_ioc_transfer))) : 0)
#define SPI_IOC_MESSAGE(N) _IOW(SPI_IOC_MAGIC, 0, char[SPI_MSGSIZE(N)])

#define SPI_SYNC_READ        _IOR(SPI_IOC_MAGIC, 10, __u32)
#define SPI_SET_WAKE_UP      _IOR(SPI_IOC_MAGIC, 20, __u32)
#define SPI_ASYNC_READ_PRE   _IOR(SPI_IOC_MAGIC, 30, __u32)
#define SPI_ASYNC_READ       _IOR(SPI_IOC_MAGIC, 40, __u32)
#define SPI_GET_BUFFER_SIZE  _IOR(SPI_IOC_MAGIC, 50, __u32)
#define SPI_HW_RESET         _IOR(SPI_IOC_MAGIC, 70, __u32)

/* Read / Write of SPI mode (SPI_MODE_0..SPI_MODE_3) */
#define SPI_IOC_RD_MODE			_IOR(SPI_IOC_MAGIC, 1, __u8)
#define SPI_IOC_WR_MODE			_IOW(SPI_IOC_MAGIC, 1, __u8)

/* Read / Write SPI bit justification */
#define SPI_IOC_RD_LSB_FIRST		_IOR(SPI_IOC_MAGIC, 2, __u8)
#define SPI_IOC_WR_LSB_FIRST		_IOW(SPI_IOC_MAGIC, 2, __u8)

/* Read / Write SPI device word length (1..N) */
#define SPI_IOC_RD_BITS_PER_WORD	_IOR(SPI_IOC_MAGIC, 3, __u8)
#define SPI_IOC_WR_BITS_PER_WORD	_IOW(SPI_IOC_MAGIC, 3, __u8)

/* Read / Write SPI device default max speed hz */
#define SPI_IOC_RD_MAX_SPEED_HZ		_IOR(SPI_IOC_MAGIC, 4, __u32)
#define SPI_IOC_WR_MAX_SPEED_HZ		_IOW(SPI_IOC_MAGIC, 4, __u32)

//
#define ARRAY_SIZE(a) (sizeof(a) / sizeof((a)[0]))


struct spi_ioc_transfer {
	__u64		tx_buf;
	__u64		rx_buf;

	__u32		len;
	__u32		speed_hz;

	__u16		delay_usecs;
	__u8		bits_per_word;
	__u8		cs_change;
	__u32		pad;
};


#endif
