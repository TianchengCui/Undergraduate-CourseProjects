def isAnagram(s1,s2):
    a=list(s1)#list the alphabets in the word
    c=sorted(a)#sort the list
    b=list(s2)
    d=sorted(b)
    if c==d:#if the sorted lists are the same, the words are anagrams
        print('is an anagram')
    else:
        print('is not an anagram')

m=input('Please enter the first word:')#ask the user to input words
n=input('Please enter the second word:')
isAnagram(m,n)
