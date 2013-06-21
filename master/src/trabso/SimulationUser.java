package trabso;

import interfaces.NotificationInterface;
import java.util.ArrayList;

public class SimulationUser implements NotificationInterface{

	// notifica a alocacao de quadros aos diferentes processos carregados na memoria
	public void displayMemoryMap(ArrayList<ListNode> memoryBlocks){
                System.out.println("Process / Base / Size");
		for(int i=0; i< memoryBlocks.size(); i++){                
                    System.out.println(memoryBlocks.get(i).getProcess() + " / " + memoryBlocks.get(i).getBase() + 
                            " / " + memoryBlocks.get(i).getSize());
                }
	}
}