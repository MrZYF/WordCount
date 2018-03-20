#include <stdio.h>

int main()
{
	float r,h,c1,s1,s2,v2,v3;
	// 输入半径和高 
	scanf("%f %f", &r, &h);
	c1 = 2*3.14*r;	// 周长 
	s1 = 3.14*r*r;	// 面积 
	s2 = 4*3.14*r*r; 
	v2 = 4/3*3.14*r*r*r;
	v3 = s1 * h;
	printf("圆周长为%.2f\n圆面积为%.2f\n圆球表面积为%.2f\n圆球体积为%.2f\n圆柱体积为%.2f\n", c1, s1, s2, v2, v3);
}
