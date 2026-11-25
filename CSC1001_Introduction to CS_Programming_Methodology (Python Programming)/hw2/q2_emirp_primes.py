def prime(n):
    for i in range(2,int(n**0.5)+1):#use math to determine whether n is a prime
        if n%i==0:
            return False
    return True
def ispa(b):
    #use logical expression to determine whether n is an emirp or not
    return prime(b) and prime(int(str(b)[::-1])) and str(b) != str(b)[::-1]
    

emirp=[]  
n= 2
time=0
while len(emirp) < 100:  #use while loop to create a list of 100 emirps
    if ispa(n):  
        emirp.append(n)
        time+=1
    n+= 1

s=''#set initial empty string
count=1
for i in emirp:
    s=s+' '+str(i)#concatenate emirps to s
    a=s.lstrip()
    count=count+1
    if count==10:
        print(a)
        count=0#when there are 10 numbers in the string, reset the values
        s=''
