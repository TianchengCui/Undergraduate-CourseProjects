def sqrt(n):
    lastGuess=1
    while True:
        nextGuess=(lastGuess+(n/lastGuess))/2#use while loop and the method given to approximate the value
        if nextGuess-lastGuess>0 and nextGuess-lastGuess<0.0001:
            return nextGuess
        elif nextGuess-lastGuess<0 and nextGuess-lastGuess>-0.0001:
            return nextGuess
        elif nextGuess-lastGuess==0:
            return nextGuess
        else:
            lastGuess=nextGuess
