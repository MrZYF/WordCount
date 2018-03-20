#include <stdio.h>

int main()
{
	int m,n;// 输入两个数 
	scanf("%d %d", &m, &n);
	int a;
	if(m>n) a = n;
	else a = m;
	for(int i=a; i>=1; i--)
	{
		if(m%i == 0 && n%i == 0)
		{
			printf("%d和%d的最大公约数是%d",m,n,i);
			break;
		}
	}
	
}
