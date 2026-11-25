m=eval(input('Please enter an integer:'))#get the number m
n=1#set an initial value for n
while n>0:#increase the number of n
    n=n+1
    if n**2>m:#when n**2>m print n and break the loop
        print(n)
        break
