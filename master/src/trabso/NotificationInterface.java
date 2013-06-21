package trabso;

import java.util.ArrayList;

public interface NotificationInterface {
	
	// notifica a alocacao de quadros aos diferentes processos carregados na memoria
	public void displayMemoryMap(int[][] memoryBlocks);
}