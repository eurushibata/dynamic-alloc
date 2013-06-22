
import exceptions.MemoryOverflow;
import java.util.logging.Level;
import java.util.logging.Logger;
import trabso.ContiguousAllocationManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Yuji
 */
public class Memory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ContiguousAllocationManager a = new ContiguousAllocationManager(6);
        try {
            //        System.out.println(a.dynamicMemory.get(0).getProcess());
                    a.allocateMemoryBlock(1, 3);
                    a.allocateMemoryBlock(2, 2);
                    a.allocateMemoryBlock(3, 1);
        } catch (MemoryOverflow ex) {
            Logger.getLogger(Memory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        a.freeAll();
        a.freeMemoryBlock(1);
//        a.freeMemoryBlock(2);
        a.freeMemoryBlock(3);
        a.compactMemory();
        for(int i=0; i<a.dynamicMemory.size(); i++) {
            System.out.println(a.dynamicMemory.get(i).getProcess() + "/" + a.dynamicMemory.get(i).getBase()+"/"+a.dynamicMemory.get(i).getSize());
        }
    }
}
