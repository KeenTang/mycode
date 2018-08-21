package com.k.list;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: keen
 * Date: 2018-08-13
 * Time: 22:32
 */
public class SimpleLinkedList<E> {
    private int size;
    private Node<E> first;
    private Node<E> last;

    static final class Node<E> {
        Node(E value) {
            this.value = value;
        }

        private Node<E> prev;
        private Node<E> next;
        public E value;

        public static <E> Node create(E value) {
            return new Node(value);
        }
    }

    public void add(E value) {
        if (first == null) {
            //first = new Node<>(value);
            //this.last = first;
            this.addFirst(value);
        } else {
            Node node = Node.create(value);
            node.prev = last;
            this.last.next = node;
            this.last = node;
            ++size;
        }
    }

    public void addFirst(E value) {
        Node node = Node.create(value);
        if (first == null) {
            first = node;
            this.last = first;
        } else {
            this.first.prev = node;
            node.next = this.first;
            this.first = node;
        }
        ++size;
    }

    public void insert(int i, E value) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("i<0");
        }
        if (i == 0) {
            this.addFirst(value);
        } else if (i >= size) {
            add(value);
        } else {
            Node<E> node = find(i);
            Node newNode = Node.create(value);
            node.prev.next = newNode;
            newNode.prev = node.prev;
            node.prev = newNode;
            newNode.next = node;
            ++size;
        }
    }

    public E get() {
        return this.get(0);
    }

    public E get(int i) {
        Node<E> node = find(i);
        return node.value;
    }

    public E remove() {
        return this.remove(0);
    }

    public E remove(int i) {
        Node<E> node = find(i);
        E value = node.value;
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        --size;
        return value;
    }

    public int size() {
        return this.size;
    }

    private Node<E> find(int i) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }
        Node node;
        if (i <= (size >> 1)) {
            node = this.first;
            for (int n = 0; n < i; n++) {
                node = node.next;
            }
        } else {
            node = this.last;
            for (int n = size - 1; n > i; n--) {
                node = node.prev;
            }
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node node = this.first;
        while (node != null) {
            sb.append(String.valueOf(node.value));
            sb.append(",");
            node = node.next;
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

}
