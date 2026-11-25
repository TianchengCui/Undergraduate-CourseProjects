import math
from math import sin#import built-in trigonometric functions
from math import cos
from math import tan
a=eval(input('Please enter the left end point a of the interval:'))#get the value of a
b=eval(input('Please enter the right end point b of the interval:'))#get the value of b
n=eval(input('Please enter the number of sub-intervals n:'))#get the value of n
fx=eval(input('Please enter a trigonometric function:'))#get the name of the function
inte=0#set the initial value of integration
i=1#set the initial value of i
if a<b and n>0 and isinstance(n, int):
    while i<=n:
        inte=inte+((b-a)/n)*fx(a+((b-a)/n)*(i-1/2))
        i=i+1#calculate the integration using while loop
    print(inte)
elif a>b:
    print('please let a<b')
elif n<0:
    print('please enter a positive integer')
else:
    print('invalid input, please run the procedure again')
 
