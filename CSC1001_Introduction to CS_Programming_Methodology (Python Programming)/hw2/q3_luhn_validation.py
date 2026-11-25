def getDigit(i):
    #return the number if it is a single digit
    if len(str(i))==1:
        return i
    #return the sum of the two digits if there are two    
    if len(str(i))==2:
        b=list(str(i))
        s=int(b[0])+int(b[1])
        return s

            
            

def sumOfDoubleEvenPlace(l):
    lst=[]
    n=1
    for i in l:
        lst.append(int(l[len(l)-2*n]))#append every second digit from right to left
        n=n+1
        if len(l)-2*n<0:#index should>=0
            break
    
    s=0
    for i in lst:
        a=getDigit(2*i)#double the digits in the list
        s=s+a#add them together
    return s#get the result

def sumOfOddPlace(l):
    lst=[]
    n=1
    for i in l:
        lst.append(l[len(l)+1-2*n])#append odd place digits to the list
        n=n+1
        if len(l)+1-2*n<0:#index should>=0
            break
    s=0
    for i in lst:
        i=int(i)
        s=s+i
    return s#return the sum of odd place digits

def isValid(n):
    #use logical expression to determine the validity of the number
    if (n.startswith('4')==False and (n.startswith('5')==False) and (n.startswith('37')==False) and (n.startswith('6')==False)) or (len(n)<13 or len(n)>16) or ((sumOfDoubleEvenPlace(list(n))+sumOfOddPlace(list(n)))%10!=0):
        return False

    else:
        return True


a=input('Please enter a credit card number:')
print(isValid(a))


    
    
        
        
    

