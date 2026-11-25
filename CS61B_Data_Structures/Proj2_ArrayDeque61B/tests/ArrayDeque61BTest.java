import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;

public class ArrayDeque61BTest {

    @Test
    public void testaddFirstaddLast(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.getFirst()).isEqualTo(null);
        assertThat(ad.getLast()).isEqualTo(null);
        ad.addFirst(121);
        ad.addFirst(99);
        assertThat(ad.getFirst()).isEqualTo(99);
        assertThat(ad.getLast()).isEqualTo(121);

        ad.addFirst(0);
        ad.addLast(100);
        assertThat(ad.getFirst()).isEqualTo(0);
        assertThat(ad.getLast()).isEqualTo(100);
    }

    @Test
    public void testisEmpty(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.isEmpty()).isTrue();
        ad.addFirst(121);
        assertThat(ad.isEmpty()).isFalse();
    }

    @Test
    public void testsize(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.size()).isEqualTo(0);
        ad.addFirst(121);
        ad.addFirst(99);
        ad.addFirst(0);
        ad.addLast(100);
        assertThat(ad.size()).isEqualTo(4);
    }

    @Test
    public void testget(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.get(1)).isEqualTo(null);
        ad.addLast(1);
        ad.addFirst(0);
        ad.addFirst(7);
        ad.addFirst(6);
        assertThat(ad.get(0)).isEqualTo(6);
        assertThat(ad.get(1)).isEqualTo(7);
        assertThat(ad.get(2)).isEqualTo(0);
        assertThat(ad.get(3)).isEqualTo(1);
        assertThat(ad.get(4)).isEqualTo(null);
    }

    @Test
    public void testGetOutofBounds() {
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        assertThat(ad.get(-1)).isEqualTo(null);
        assertThat(ad.get(100)).isEqualTo(null);
        assertThat(ad.get(2)).isEqualTo(null);
    }

    @Test
    public void testToListEmpty() {
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.toList()).isEmpty();
    }

    @Test
    public void testtoList(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addFirst(0);
        ad.addFirst(7);
        ad.addFirst(6);
        assertThat(ad.toList()).containsExactly(6,7,0,1);
    }

    @Test
    public void testremoveFirst(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.removeFirst()).isEqualTo(null);
        ad.addLast(1);
        ad.addFirst(0);
        ad.addFirst(7);
        ad.addFirst(6);
        assertThat(ad.removeFirst()).isEqualTo(6);
    }

    @Test
    public void testremoveLast(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.removeLast()).isEqualTo(null);
        ad.addLast(1);
        ad.addFirst(0);
        ad.addFirst(7);
        ad.addFirst(6);
        assertThat(ad.removeLast()).isEqualTo(1);
    }

    @Test
    public void testRemoveToEmptyAndOne() {
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);
        ad.addLast(4);

        ad.removeLast();
        ad.removeLast();
        ad.removeLast();

        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.get(0)).isEqualTo(1);

        ad.removeLast();
        assertThat(ad.isEmpty()).isTrue();
        assertThat(ad.removeLast()).isEqualTo(null);

        ad.addFirst(1);
        ad.addFirst(2);
        ad.addFirst(3);

        ad.removeFirst();
        ad.removeFirst();
        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.get(0)).isEqualTo(1);
        ad.removeFirst();
        assertThat(ad.isEmpty()).isTrue();
    }

    @Test
    public void testAddAfterRemoveToEmpty() {
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addFirst(1);
        ad.addLast(2);
        ad.removeFirst();
        ad.removeLast();
        assertThat(ad.isEmpty()).isTrue();
        assertThat(ad.size()).isEqualTo(0);
        ad.addFirst(3);
        assertThat(ad.get(0)).isEqualTo(3);
        assertThat(ad.size()).isEqualTo(1);
        ad.removeFirst();
        ad.addLast(4);
        assertThat(ad.get(0)).isEqualTo(4);
        assertThat(ad.size()).isEqualTo(1);
    }

    @Test
    public void testupResizing(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addFirst(0);
        ad.addFirst(7);
        ad.addFirst(6);
        ad.addFirst(5);
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(8);
        ad.addLast(9);
        assertThat(ad.get(0)).isEqualTo(8);
        assertThat(ad.get(9)).isEqualTo(9);
        assertThat(ad.size()).isEqualTo(10);
        assertThat(ad.toList()).containsExactly(8,2,3,4,5,6,7,0,1,9);
    }

    @Test
    public void testdownResizing(){
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addFirst(0);
        ad.addFirst(7);
        ad.addFirst(6);
        ad.addFirst(5);
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(8);
        ad.addLast(9);
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        assertThat(ad.get(0)).isEqualTo(9);
        assertThat(ad.size()).isEqualTo(1);
        assertThat(ad.toList()).containsExactly(9);
    }

    @Test
    public void testRemoveLastTriggerResize() {
        Deque61B<Integer> ad = new ArrayDeque61B<>();
        for (int i = 0; i < 16; i++) {
            ad.addLast(i);
        }
        for (int i = 0; i < 12; i++) {
            ad.removeLast();
        }
        assertThat(ad.size()).isEqualTo(4);
        assertThat(ad.get(0)).isEqualTo(0);
        assertThat(ad.getLast()).isEqualTo(3);
    }

    @Test
    public void testIterator() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);

        java.util.List<Integer> actual = new java.util.ArrayList<>();
        for (int x : ad) {
            actual.add(x);
        }
        assertThat(actual).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void testEqualDeques61B() {
        Deque61B<String> ad = new ArrayDeque61B<>();
        Deque61B<String> ad2 = new ArrayDeque61B<>();

        ad.addLast("front");
        ad.addLast("middle");
        ad.addLast("back");

        ad2.addLast("front");
        ad2.addLast("middle");
        ad2.addLast("back");

        assertThat(ad).isEqualTo(ad2);
    }

    @Test
    public void testToStringEmpty() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        assertThat(ad.toString()).isEqualTo("[]");
    }

    @Test
    public void testToStringMultiple() {
        ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
        ad.addLast(1);
        ad.addLast(2);
        ad.addLast(3);
        assertThat(ad.toString()).isEqualTo("[1, 2, 3]");
    }

}
