package model.ds;

public class Stack<T> {
    private static class Node<T> { T v; Node<T> next; Node(T v){this.v=v;} }
    private Node<T> top; private int size;
    public void push(T v){ Node<T> n=new Node<>(v); n.next=top; top=n; size++; }
    public T pop(){ if(top==null) throw new IllegalStateException("Pila vacía");
        T v=top.v; top=top.next; size--; return v; }
    public T peek(){ if(top==null) throw new IllegalStateException("Pila vacía"); return top.v; }
    public boolean isEmpty(){ return top==null; }
    public int size(){ return size; }
    public T tryPop(){ return top==null ? null : pop(); }
    public void clear(){ while(top!=null) top=top.next; size=0; }
}
