#include <stdio.h>

int main()
{	// 设定范围为1900-2000 
	for(int i=1900; i<=2000; i+=4)
	{
		if(i%100 != 0 || i%400 == 0)
			printf("%d是闰年\n", i);
		else
			printf("%d不是闰年\n", i);
		if(i!=2000)
			printf("%d不是闰年\n%d不是闰年\n%d不是闰年\n", i+1, i+2, i+3);	
	}
}
