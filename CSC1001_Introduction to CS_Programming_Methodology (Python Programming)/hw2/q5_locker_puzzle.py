l=[False]*100
def f(n):
    for i in range(n,101,n):#start with the nth locker and change every n locker
        global l
        l[i-1]=not l[i-1]
    return l

for n in range(1,101):#go through 100 students
    f(n)
print(l)
        
