package interfaces;

import exceptions.MemoryOverflow;
import exceptions.InvalidAddress;

public interface ManagementInterface {

	// aloca um bloco de memoria para um processo 
	public boolean allocateMemoryBlock(int processId, int size) throws MemoryOverflow;

	// libera um bloco de memoria ocupado por um processo
	public boolean freeMemoryBlock(int processId);
	
	// libera todos os blocos de memoria ocupados
	public void freeAll();

 	// redistribui o conteudo da memoria de modo a criar um grande e unico bloco de memoria livre
	public void compactMemory();
	
	// traduz um endereco logico de um processo para um endereco fisico
	public int getPhysicalAddress(int processId, int logicalAddress) throws InvalidAddress;

	// processa um arquivo texto contendo um conjunto de comandos
	public boolean processCommandFile(String fileName);
}