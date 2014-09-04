package restaurant.restaurantGabe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import restaurant.restaurantGabe.CookRole;

public class RevolvingStand {

	Vector orders;// = new ArrayList<Order>();
	CookRole cook;
	
	
	public void setCook(CookRole c){
		this.cook = c;
	}
	
	
	
	private final int N = 10;
    private int count = 0;
    //private Vector orders;
    
    
    synchronized public void insert(Order data) {
        while (count == N) {
            try{ 
                System.out.println("\tFull, waiting");
                wait(5000);                         // Full, wait to add
            } catch (InterruptedException ex) {};
        }
            
        insert_item(data);
        count++;
        if(count == 1) {
            System.out.println("\tNot Empty, notify");
            notify();                               // Not empty, notify a 
                                                    // waiting consumer
        }
    }
    
    synchronized public Order remove() {
        Order data;
        
        //added so that the cook won't wait forever for new orders
        if(orders.isEmpty()){
        	return null;
        }
        
        while(count == 0)
            try{ 
                System.out.println("\tEmpty, waiting");
                wait(5000);                         // Empty, wait to consume
            } catch (InterruptedException ex) {};
 
        data = remove_item();
        count--;
        if(count == N-1){ 
            System.out.println("\tNot full, notify");
            notify();                               // Not full, notify a 
                                                    // waiting producer
        }
        return data;
    }
    
    private void insert_item(Order data){
        orders.addElement(data);
    }
    
    private Order remove_item(){
        Order data = (Order) orders.firstElement();
        orders.removeElementAt(0);
        return data;
    }
    
    public RevolvingStand(){
        orders = new Vector();
    }
	
	
	
	public void addOrder(Order o){
		
		insert(o);
		System.out.println("Added a new order. There are now "+count+" orders on the stand");
		if(cook!=null){
			cook.msgStateChanged();
		}
	}
	
	
	
}
