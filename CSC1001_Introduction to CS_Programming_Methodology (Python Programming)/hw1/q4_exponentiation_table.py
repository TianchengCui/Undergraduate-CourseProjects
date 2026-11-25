p=eval(input('Please enter a positive integer number:'))#get an integer
if p>0 and type(p)==int:#if the input is valid, run the procedure
    print('%-9s%-9s%-9s'%('m','m+1','m**(m+1)'))#use formatted output to print the line with m
    n=1#set an initial number for n
    while n<=p:#use while loop to print lines from 1 to n
        print('%-9s%-9s%-9s'%(n,n+1,n**(n+1)))
        n=n+1
else:
    print('Invalid input, please run it again')#if the input is invalid, inform the user to put in valid ones


