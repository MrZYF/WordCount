#include <stdio.h>

int main()
{
	float r,h,c1,s1,s2,v2,v3;
	// ����뾶�͸� 
	scanf("%f %f", &r, &h);
	c1 = 2*3.14*r;	// �ܳ� 
	s1 = 3.14*r*r;	// ��� 
	s2 = 4*3.14*r*r; 
	v2 = 4/3*3.14*r*r*r;
	v3 = s1 * h;
	printf("Բ�ܳ�Ϊ%.2f\nԲ���Ϊ%.2f\nԲ������Ϊ%.2f\nԲ�����Ϊ%.2f\nԲ�����Ϊ%.2f\n", c1, s1, s2, v2, v3);
}
