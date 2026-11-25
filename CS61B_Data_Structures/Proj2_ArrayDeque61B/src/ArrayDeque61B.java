import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T>, Iterable<T>{
    private T[] item;
    private int nextFirst;
    private int nextLast;
    private  int size;

    public ArrayDeque61B() {
        item = (T[]) new Object[8];
        nextFirst = 0;
        nextLast = 1;
        size = 0;
    }

    public void resizeUp(int capacity){
        T[] resized = (T[]) new Object[capacity];
        for (int i = 0; i<size; i++){
            resized[i] = get(i);
        }
        item = resized;
        nextFirst = resized.length-1;
        nextLast = size;
    }

    /**
     * Add {@code x} to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addFirst(T x) {
        if (size == item.length){
            resizeUp(size * 2);
        }
        item[nextFirst] = x;
        nextFirst -= 1;
        if (nextFirst == -1){
            nextFirst = item.length-1;
        }
        size += 1;
    }

    /**
     * Add {@code x} to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addLast(T x) {
        if (size == item.length){
            resizeUp(size * 2);
        }
        item[nextLast] = x;
        nextLast += 1;
        size += 1;
        if (nextLast == item.length){
            nextLast = 0;
        }
    }

    /**
     * Returns a List copy of the deque. Does not alter the deque.
     *
     * @return a new list copy of the deque.
     */
    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size(); i++){
            returnList.add(get(i));
        }
        return returnList;
    }

    /**
     * Returns if the deque is empty. Does not alter the deque.
     *
     * @return {@code true} if the deque has no elements, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        if (size == 0){
            return true;
        }
        return false;
    }

    /**
     * Returns the size of the deque. Does not alter the deque.
     *
     * @return the number of items in the deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Return the element at the front of the deque, if it exists.
     *
     * @return element, otherwise {@code null}.
     */
    @Override
    public T getFirst() {
        if (nextFirst == item.length-1){
            return item[0];
        }
        if (nextFirst+1 == nextLast){
            return null;
        }
        return item[nextFirst + 1];
    }

    /**
     * Return the element at the back of the deque, if it exists.
     *
     * @return element, otherwise {@code null}.
     */
    @Override
    public T getLast() {
        if (nextLast == 0){
            return item[item.length-1];
        }
        if (nextFirst+1 == nextLast){
            return null;
        }
        return item[nextLast - 1];
    }

    public void resizeDown(int capacity){
        T[] resized = (T[]) new Object[capacity];
        for (int i = 0; i<size; i++){
            resized[i] = get(i);
        }
        item = resized;
        nextFirst = resized.length-1;
        nextLast = size;
    }

    /**
     * Remove and return the element at the front of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeFirst() {
        if (isEmpty() == true){
            return null;
        }
        T returnFirst = get(0);
        if (item.length>4*size){
            resizeDown(size+1);
        }
        nextFirst += 1;
        if (nextFirst == item.length){
            nextFirst =0;
        }
        size -= 1;
        return returnFirst;
    }

    /**
     * Remove and return the element at the back of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeLast() {
        if (isEmpty() == true){
            return null;
        }
        T returnLast = get(size-1);
        if (item.length>4*size){
            resizeDown(size+1);
        }
        nextLast -= 1;
        if (nextLast == -1){
            nextLast = item.length-1;
        }
        size -= 1;
        return returnLast;
    }

    /**
     * The Deque61B abstract data type does not typically have a get method,
     * but we've included this extra operation to provide you with some
     * extra programming practice. Gets the element, iteratively. Returns
     * null if index is out of bounds. Does not alter the deque.
     *
     * @param index index to get
     * @return element at {@code index} in the deque
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        int realIndex = nextFirst + 1 + index;

        if (realIndex >= item.length) {
            realIndex -= item.length;
        }
        return item[realIndex];
    }

    /**
     * This method technically shouldn't be in the interface, but it's here
     * to make testing nice. Gets an element, recursively. Returns null if
     * index is out of bounds. Does not alter the deque.
     *
     * @param index index to get
     * @return element at {@code index} in the deque
     */
    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for ArrayDeque61B.");
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    public class ArrayDequeIterator implements Iterator<T>{
        private int wizPos;

        public ArrayDequeIterator(){
            wizPos = 0;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         */
        @Override
        public T next() {
            T returnvalue = get(wizPos);
            wizPos +=1;
            return returnvalue;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque61B<?> otherDeque)) {
            return false;
        }
        if (this.size() != otherDeque.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(this.get(i).equals(otherDeque.get(i)))) {
                return false;
            }
        }
        return true;
    }

    private boolean contains(T x) {
        for (int i = 0; i<size; i++){
            if (get(i).equals(x)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        String returnString = "[";
        if (!isEmpty()){
            for (int i = 0; i<size-1; i++){
                returnString += get(i);
                returnString += ", ";
            }
            returnString += get(size-1);
        }
        returnString += "]";
        return returnString;
    }
}