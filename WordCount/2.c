#include <stdio.h>

int main()
{
	int m,n;// ���������� 
	scanf("%d %d", &m, &n);
	int a;
	if(m>n) a = n;
	else a = m;
	for(int i=a; i>=1; i--)
	{
		if(m%i == 0 && n%i == 0)
		{
			printf("%d��%d�����Լ����%d",m,n,i);
			break;
		}
	}
	
}
