#include <stdio.h>
#include <math.h>

int main()
{	// ѭ�� 
	for(int n=100; n<=200; n++)
	{
		for(int i=2; i<=sqrt(n); i++)
		{
			if(n%i == 0)
				break;
			if(i>sqrt(n)-1)
				printf("%d������\n",n);
		}
	}
}
