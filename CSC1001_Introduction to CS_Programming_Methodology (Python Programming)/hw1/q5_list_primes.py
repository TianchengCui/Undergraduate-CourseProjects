def prime(n):#define a function
    if n<=1:#no prime, return false
        return False
    for i in range(2,n-1):#if n is divided by all numbers in(2,n-1)and have remain all the time, it is a  prime
        if n%i==0:
            return False
    return True

try:
    n=eval(input('Please enter a positive integer:'))
    time=0#set an initial value
    listt=[]#set an initial list
    for i in range(2,n):#evaluate all numbers in(2,n)
        if prime(i):
            time+=1
            listt.append(i)#add the prime to the list
            if time==8:
                print(*listt,end='\n')#stop this line and start a new one
                time=0#reset the initial value
                listt=[]#reset the list
    print(*listt)#print the whole list
except:
    print('Please try again,input a positive integer')
        
    
