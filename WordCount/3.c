#include <stdio.h>

int main()
{	// �趨��ΧΪ1900-2000 
	for(int i=1900; i<=2000; i+=4)
	{
		if(i%100 != 0 || i%400 == 0)
			printf("%d������\n", i);
		else
			printf("%d��������\n", i);
		if(i!=2000)
			printf("%d��������\n%d��������\n%d��������\n", i+1, i+2, i+3);	
	}
}
