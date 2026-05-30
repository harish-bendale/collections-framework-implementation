package collection_framework;

import java.util.ArrayList;
import java.util.Map;

class Node<K, V> {
	
	public long hashCode;
	public final K key;
	public V value;
	public Node<K, V> next;
	
	Node(long hashCode, K key, V value) {
		this.hashCode = hashCode;
		this.key = key;
		this.value = value;
	}
	
	void updateValue(V value) {
		this.value = value;
	}
}
class HashMapCustom<K, V> {
	private final double loadFactor = 0.75;
	private int size = 0;
	
	@SuppressWarnings("unchecked")
	Node<K,V>[] hashMap = (Node<K, V>[]) new Node[16];
	
	void resize() {
		int lengthOfHashMap = hashMap.length;
		
		if(lengthOfHashMap >= 1e5) {
			return ;
		}
		lengthOfHashMap<<=1;
		@SuppressWarnings("unchecked")
		Node<K,V>[] newMap = (Node<K, V>[])new Node[lengthOfHashMap];
		
		for(int i=0; i<hashMap.length; i++) {
			Node<K,V> current = hashMap[i];
			
			while(current != null) {
				Node<K, V> next = current.next; 
				
				long hashCode = current.hashCode;
				K key = current.key;
				V value = current.value;
				
				int index = calculateIndex(newMap.length, hashCode);
				
				if(newMap[index] == null) {
					newMap[index] = current;
					current.next = null;
				}else {
					Node<K, V> t1 = newMap[index];
					newMap[index] = current;
					current.next = t1;
				}
				current = next;
				if(next != null) next = next.next;
			}
		}
		hashMap = newMap;
	}
	
	@SuppressWarnings("unchecked")
	private boolean putUtil(boolean replace, boolean putIfAbsent, long hashCode, int index, K key, V value) {
		if(hashMap[index] == null && replace == false) {
			hashMap[index] = (Node<K, V>)new Node<K, V>(hashCode, key, value);
		}else {
			Node<K,V> existingNode = get(hashCode, key);
			if(existingNode != null) {
				if(putIfAbsent == false) existingNode.updateValue(value);
				return false;
			}
			
			if(replace == false) {
				Node<K, V> temp = hashMap[index];
				hashMap[index] = new Node<K, V>(hashCode, key, value);
				hashMap[index].next = temp;
			}
		}
		return true;
	}
	
	private int calculateIndex(int lengthOfHashMap, long hashCode) {
		return (int)((hashCode & Long.MAX_VALUE)%lengthOfHashMap);
	}
	
	private long calculateHashCode(K key) {
		 Long hashCode = 0L;
		 
		 if(key == null) return 0L;
		 return key.hashCode();
	 }
	
	Node<K, V> get(long hashCode, K key) {
		int index = calculateIndex(hashMap.length, hashCode);
		
		
		Node<K, V> temp = hashMap[index];
		
		
		while(temp != null) {
			if(temp.hashCode == hashCode) {
				if(temp.key.equals(key)) {
					return temp;
				}
			}
			temp = temp.next;
		}
		return null;
	}
	
	void put(K key, V value) {
		
		if((size+1 > loadFactor  * hashMap.length)) {
			resize();
			System.out.println(size + " Resize Resize Resize");
		}
		
		long hashCode = calculateHashCode(key);
		
		int index = calculateIndex(hashMap.length, hashCode);
		
		if(putUtil(false, false, hashCode, index, key, value)) size++;
		
		
	}
	
	V getValue(K key) {
		
		long hashCode = calculateHashCode(key);
		Node<K, V> node = get(hashCode, key);
		
		return node == null ? null : node.value;
	}
	
	void remove(K key) {
		long hashCode = calculateHashCode(key);
		int index = calculateIndex(hashMap.length, hashCode);
		
		Node<K, V> current = hashMap[index];
		Node<K, V> previous = null;
		
		while(current != null) {
			if(current.hashCode == hashCode) {
				
				if(current.key.equals(key)) {
					if(previous == null) {
						hashMap[index] = current.next;
						
					}else {
						previous.next = current.next;
					}
					size--;
					return ;
				}
			}
			
			previous = current;
			current = current.next;
		}
	}
	
	boolean containsValue(V value) {
		for(int i=0; i<hashMap.length; i++) {
			Node<K, V> temp = hashMap[i];
			
			while(temp != null) {
				if(temp.value.equals(value)) {
					return true;
				}
				temp = temp.next;
			}
		}
		
		return false;
	}
	
	void putIfAbsent(K key, V value) {
		long hashCode = calculateHashCode(key);
		int index = calculateIndex(hashMap.length, hashCode);
		if(putUtil(false, true, hashCode, index, key, value)) size++;
	}
	
	V getOrDefault(K key, V defaultValue) {
		V value = getValue(key);
		return value == null ? defaultValue : value;
	}
	
	void replace(K key, V value) {
		long hashCode = calculateHashCode(key);
		int index = calculateIndex(hashMap.length, hashCode);
		
		putUtil(true, false, hashCode, index, key, value);
	}
	
	boolean containsKey(K key) {
		if(getValue(key) != null) return true;
		return false;
	}
	
	int size() {
		return this.size;
	}
	
	boolean isEmpty() {
		return size == 0;
	}
	
	@SuppressWarnings("unchecked")
	void clear() {
		hashMap = (Node<K, V>[]) new Node[16];
		size = 0;
	}
	
	ArrayList<K> keySet() {
		ArrayList<K> keyset = new ArrayList<>();
		
		for(int i=0; i<hashMap.length; i++) {
			Node<K, V> temp = hashMap[i];
			
			while(temp != null) {
				keyset.add(temp.key);
				temp = temp.next;
			}
		}
		
		return keyset;
	}
	
	ArrayList<V> values() {
		ArrayList<V> values = new ArrayList<>();
		
		for(int i=0; i<hashMap.length; i++) {
			Node<K, V> temp = hashMap[i];
			
			while(temp != null) {
				values.add(temp.value);
				temp = temp.next;
			}
		}
		
		return values;
	}
}
public class HashMap {
	 
	 public static void main(String[] args) {
		 	
	        HashMapCustom<String, String> hashMap = new HashMapCustom<>();
	        
	        
	       ArrayList<String> first = new ArrayList<>();
	       ArrayList<String> second = new ArrayList<>();
	       
	       for(int i=0; i<1000; i++) {
	    	   first.add("Person_" + i);
	    	   second.add("City_" + i);
	       }
	       
	       for(int i=0; i<1000; i++) {
	    	   hashMap.put(first.get(i), second.get(i));
	       }
	      
	       for(int i=0; i<50; i++) {
	    	   hashMap.remove(first.get(i));
	       }
	       
	       for(int i=0; i<100; i++) {
	    	   System.out.println(hashMap.getValue("Person_" + i));
	       }
	       
	       
	       
	       hashMap.put("Harish", "Pune");
	        hashMap.put("Prathamesh", "Amalner");
	        hashMap.put("Chaitanya", "Shirur");
	        hashMap.put("", "Bhusawal");
	        hashMap.put("Harish", "Bhusawal");
	       System.out.println(hashMap.getValue("Harish"));
	       System.out.println(hashMap.getValue("Prathamesh"));
	       System.out.println(hashMap.getValue("Chaitanya"));
	       System.out.println(hashMap.getValue("Prakash"));
	       System.out.println(hashMap.getValue(""));
	       System.out.println(hashMap.size());
	       
	       hashMap.replace("Prasad", "Borole");
	       System.out.println(hashMap.getValue("Prasad"));
	}
}

