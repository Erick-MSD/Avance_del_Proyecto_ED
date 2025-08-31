package model.ds;

public class Queue<T> {
    private static class Node<T>{ T v; Node<T> next; Node(T v){this.v=v;} }
    private Node<T> head, tail; private int size;
    public void enqueue(T v){ Node<T> n=new Node<>(v); if(tail==null){head=tail=n;} else {tail.next=n; tail=n;} size++; }
    public T dequeue(){ if(head==null) throw new IllegalStateException("Cola vacía");
        T v=head.v; head=head.next; if(head==null) tail=null; size--; return v; }
    public T front(){ if(head==null) throw new IllegalStateException("Cola vacía"); return head.v; }
    public boolean isEmpty(){ return head==null; }
    public int size(){ return size; }
}
