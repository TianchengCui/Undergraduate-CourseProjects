"""
You are only permitted to write code between Start and End.
Don't change the template. Otherwise, you will get zero point.
Don't write any `print` function in your code. 
"""


## Here you can write some extra code if needed, or you can igore this block.
# Start writing your code.

# End writing your code.


def HanoiTower(n, from_rod ='A',aux_rod ='B',to_rod ='C'):
    result_list = []
    # Start writing your code.


    stack = [(n, from_rod, aux_rod, to_rod)]#initialize the stack
        


    while stack:#while stack is not empty, pop the condition out of the stack
        n, from_rod, aux_rod, to_rod=stack.pop()

        if n != 1:
            stack.append((n - 1, aux_rod, from_rod, to_rod))#implement the common move of solving the hanoi tower
            stack.append((1, from_rod, aux_rod, to_rod))#append the condition into the stack
            stack.append((n - 1, from_rod, to_rod, aux_rod))

        else:#when there is only 1 in the tower move it to rod C
            step={'from':from_rod,'to':to_rod}
            result_list.append(step)



    # End writing your code.
    return result_list



"""
You should store each line your output in result_list defined above.
For example, if the steps of your HanoiTower are: 
                A --> C
                A --> B
then you must store them in result_list by writing: 

step = {'from': 'A', 'to': 'C'}
result_list.append(step)
step = {'from': 'A', 'to': 'B'}
result_list.append(step)

Thus, once you determine one step, please store it in a dict which includes keys 'from' and 'to', and then add the dict to the result_list. 

Don't write any `print()` function in your code, since we only write code as follows to check your answer. You can remain or delete the following code in the submission. Because the following code will be rewritten when checking your answer.
"""
if __name__ == '__main__':
    n = 3  # An example. We will change it during testing.
    result_list = HanoiTower(n)
    for step in result_list:
        print(step['from'], '-->', step['to'])
