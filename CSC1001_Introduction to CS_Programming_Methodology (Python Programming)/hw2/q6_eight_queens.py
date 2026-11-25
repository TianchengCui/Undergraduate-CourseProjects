def chessboard():
    for x in range(0,8):
        for y in range(0,8):
            print("|", end="")
            if(c[x][y]==1):
               print("Q",end="")
            else:
               print(" ",end="")
        print("|")

def attack_or_not(row,column):
    for x in range(0,8):
        for y in range(0,8):
            if c[x][y]==1 and abs(x-row)==abs(y-column):#check in diagonals
                return False
        for i in range(0,8):
            if c[i][column]==1:#check columns in the sequence of rows
                return False
    return True

def solution(row):
    for co in range(0,8):
        if attack_or_not(row,co)==True:
            c[row][co]=1
            if solution(row+1)==True:#use recursion
                return True
            else:
                c[row][co]=0
    if row==8:#base case of the recursion
        chessboard()#print the board
        return True

c=[[1,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0],
   [0,0,0,0,0,0,0,0]]

solution(1)
