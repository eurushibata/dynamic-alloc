
import exceptions.MemoryOverflow;
import java.util.logging.Level;
import java.util.logging.Logger;
import trabso.ContiguousAllocationManager;
import exceptions.InvalidAddress;

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
//        System.out.println(a.dynamicMemory.get(0).getProcess());
        int pa=0;
        //a.allocateMemoryBlock(1, 3);
        //a.allocateMemoryBlock(2, 2);
        //a.allocateMemoryBlock(3, 1);
        a.processCommandFile("teste.txt");
        /*try{
        System.out.println("oi");
        pa = a.getPhysicalAddress(2, 1);
        System.out.println("Endereco Fisico: " + pa);
        } catch(InvalidAddress ia){
            System.err.println("erro: " + ia.getMessage());  
        }
        System.out.println("oi");
        a.freeAll();
        a.freeMemoryBlock(1);
        a.freeMemoryBlock(2);
        a.freeMemoryBlock(3);*/
        for(int i=0; i<a.dynamicMemory.size(); i++) {
            System.out.println(a.dynamicMemory.get(i).getProcess() + "/" + a.dynamicMemory.get(i).getBase()+"/"+a.dynamicMemory.get(i).getSize());
        }
    }
}
