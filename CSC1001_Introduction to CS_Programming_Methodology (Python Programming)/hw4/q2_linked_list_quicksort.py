"""
You are only permitted to write code between Start and End.
Don't change the template. Otherwise, you will get zero point.
"""


## Here you can write some extra code if needed, or you can igore this block.
# Start writing your code.

# End writing your code.


class Node:
    def __init__(self, element, pointer):
        self.element = element
        self.pointer = pointer


class SinglyLinkedList:
    def __init__(self):
        self.head = None
        self.tail = None
        self.size = 0

    def insert(self, data):
        # Start writing your code.
        node=Node(data,None)
        if self.size==0:
            self.head=node
            self.size=self.size+1
            self.tail=node
        else:
            node.pointer=self.head#insert the node to the head
            self.head=node
            self.size=self.size+1
    
        # End writing your code.


    ## Here you can write some other functions inside the class if needed, or you can igore this block.
    # Start writing your code.

        

    # End writing your code.
    

def quick_sort(node):
    # Start writing your code.
    test_list=SinglyLinkedList()
    while node.pointer!=None:
        test_list.insert(node.element)
        node=node.pointer
    if node.pointer==None:
        test_list.insert(node.element)#insert the node into singly linked list
    
        
        
    if test_list.size<=1:
        return node
    
    t=test_list.head
    p=test_list.head
    q=p.pointer

    a=[]

    while q!=None:
        if q.element<t.element:
            p.pointer=q.pointer
            test_list.size=test_list.size-1
            a.append(q.element)
            q=p.pointer
        else:
            p=q
            q=p.pointer

    test_list.head=test_list.head.pointer
    test_list.size=test_list.size-1
    a.sort(reverse=True)
    al=SinglyLinkedList()
    for i in a:
        al.insert(i)#create a singly linked list of elements smaller than the head

    b=[]
    noderight=t
    while noderight !=None:
        b.append(noderight.element)
        noderight=noderight.pointer
    b.sort(reverse=True)
    ar=SinglyLinkedList()
    for i in b:
        ar.insert(i)#create a singly linked list of elements bigger than the head

    al.tail.pointer=ar.head#put the left list,the head,and the right list together
    test_list.head=al.head
    test_list.tail=ar.tail
    

    return test_list.head
    
        
        
    # End writing your code.


# We will write code as follows to check your answer. You can remain or delete the following code in the submission. Because the following code will be rewritten when checking your answer.
if __name__ == '__main__':
    test_list = SinglyLinkedList()
    nums = [4,2,3,1,0,6,8]  # An example. We will change it during testing.
    for num in nums:
        test_list.insert(num)
    first_node = test_list.head  # Get the first node of the linked list.
    p = quick_sort(first_node)
    while p.pointer != None:
        print(p.element)
        p = p.pointer
    print(p.element)
    
