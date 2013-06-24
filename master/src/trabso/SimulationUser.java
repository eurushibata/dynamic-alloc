package trabso;

import interfaces.NotificationInterface;
import java.util.ArrayList;

/**
 * 
 * @author Yuji
 */
public class SimulationUser implements NotificationInterface{
    
    /**
     * 
     */
    public void SimulationUser() {
    }
            
    /**
     * Notifica a alocação de quadros aos diferentes processos carregados na memória.
     * 
     * @param memoryBlocks 
     */
    public void displayMemoryMap(int[][] memoryBlocks){
        System.out.println("Processo   Base\t   Tamanho");
        for(int i=0; i<memoryBlocks.length; i++){
            System.out.println("   " + memoryBlocks[i][0] + "\t     " + memoryBlocks[i][1]+"\t      "+memoryBlocks[i][2]);
        }
        System.out.println("");
    }
}