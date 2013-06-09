/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabso;

/**
 *
 * @author Yuji
 */
public class ListNode {
    private int process;
    private int base;
    private int size;

    public ListNode() {
    }
    
    
    public ListNode(int process, int base, int size) {
        this.process = process;
        this.base = base;
        this.size = size;
    }
    
    public int getProcess(){
        return process;
    }

    public int getBase(){
        return base;
    }
    
    public int getSize(){
        return size;
    }
    
    public void setProcess(int process){
        this.process = process;
    }
    
    public void setBase(int base){
        this.base = base;
    }
    
    public void setSize(int size){
        this.size = size;
    }
}
