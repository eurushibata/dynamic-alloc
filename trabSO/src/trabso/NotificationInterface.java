package trabSO;

import java.util.ArrayList;
import trabso.ListNode;

public interface NotificationInterface {
	
	// notifica a alocacao de quadros aos diferentes processos carregados na memoria
	public void displayMemoryMap(ArrayList<ListNode> memoryBlocks);
}