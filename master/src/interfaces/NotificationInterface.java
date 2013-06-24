package interfaces;

import java.util.ArrayList;

/**
 * Interface implementada pela Simulação do Usuário
 * @author Yuji
 */
public interface NotificationInterface {
	
   /**
     * Notifica a alocação de quadros aos diferentes processos carregados na memória.
     * 
     * @param memoryBlocks 
     */
    public void displayMemoryMap(int[][] memoryBlocks);
}