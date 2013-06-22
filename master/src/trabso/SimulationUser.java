package trabso;

import interfaces.NotificationInterface;
import java.util.ArrayList;

public class SimulationUser implements NotificationInterface{
    
    public void SimulationUser() {
    }
            
    // notifica a alocacao de quadros aos diferentes processos carregados na memoria
    public void displayMemoryMap(int[][] memoryBlocks){
        System.out.println("Processo   Base\t   Tamanho");
        for(int i=0; i<memoryBlocks.length; i++){
            System.out.println("   " + memoryBlocks[i][0] + "\t     " + memoryBlocks[i][1]+"\t      "+memoryBlocks[i][2]);
        }
	}
}