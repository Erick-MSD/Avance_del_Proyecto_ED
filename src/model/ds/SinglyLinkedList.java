package model.ds;

import java.util.function.Predicate;

public class SinglyLinkedList<T> {
    private static class Node<T>{ T v; Node<T> next; Node(T v){this.v=v;} }
    private Node<T> head; private int size;

    public void insert(T v){ Node<T> n=new Node<>(v); n.next=head; head=n; size++; } // inicio O(1)
    public boolean deleteFirstMatch(Predicate<T> pred){
        Node<T> prev=null, cur=head;
        while(cur!=null){
            if(pred.test(cur.v)){ if(prev==null) head=cur.next; else prev.next=cur.next; size--; return true; }
            prev=cur; cur=cur.next;
        }
        return false;
    }
    public T findFirst(Predicate<T> pred){
        Node<T> cur=head; while(cur!=null){ if(pred.test(cur.v)) return cur.v; cur=cur.next; } return null;
    }
    public int size(){ return size; }
    @Override public String toString(){
        StringBuilder sb=new StringBuilder("["); Node<T> c=head;
        while(c!=null){ sb.append(c.v); c=c.next; if(c!=null) sb.append(", "); }
        return sb.append("]").toString();
    }
}
