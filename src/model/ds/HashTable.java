package model.ds;
import java.util.LinkedList;

public class HashTable<K,V> {
    private static class Entry<K,V>{ K k; V v; Entry(K k,V v){this.k=k;this.v=v;} }
    private final LinkedList<Entry<K,V>>[] b; private int size;

    @SuppressWarnings("unchecked")
    public HashTable(int capacity){ b=new LinkedList[capacity]; for(int i=0;i<capacity;i++) b[i]=new LinkedList<>(); }
    private int idx(K k){ return (k==null?0:Math.abs(k.hashCode())) % b.length; }

    public void put(K k, V v){
        var L=b[idx(k)];
        for(var e: L){ if((k==null && e.k==null) || (k!=null && k.equals(e.k))){ e.v=v; return; } }
        L.add(new Entry<>(k,v)); size++;
    }
    public V get(K k){
        var L=b[idx(k)];
        for(var e: L){ if((k==null && e.k==null) || (k!=null && k.equals(e.k))) return e.v; }
        return null;
    }
    public boolean remove(K k){
        var L=b[idx(k)]; var it=L.iterator();
        while(it.hasNext()){ var e=it.next(); if((k==null && e.k==null) || (k!=null && k.equals(e.k))){ it.remove(); size--; return true; } }
        return false;
    }
    public int size(){ return size; }
}
