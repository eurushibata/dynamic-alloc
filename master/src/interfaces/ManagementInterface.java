package interfaces;

import exceptions.MemoryOverflow;
import exceptions.InvalidAddress;

/**
 * 
 * @author Yuji
 */
public interface ManagementInterface {

    /**
     * Aloca um bloco de memória para um processo de acordo com uma política de localização de blocos livres.
     * 
     * @param processId Identificação do processo que será alocado.
     * @param size Tamanho do Processo alocado.
     * @return Retorna true se o processo foi alocado com sucesso ou false se o processo não pode ser alocado.
     * @throws MemoryOverflow
     */
    public boolean allocateMemoryBlock(int processId, int size) throws MemoryOverflow;

    /**
     * Libera um bloco de memória ocupado por um processo.
     * 
     * @param processId Identificação do processo que será removido.
     * @return Retorna true se um bloco de memória foi removido com sucesso ou false se o bloco não pode ser removido.
     */
    public boolean freeMemoryBlock(int processId);
	
    /**
      * Libera todos os blocos de memória ocupados.
      */
    public void freeAll();

    /**
     * Redistribui o conteúdo da memória de modo a criar um grande e único bloco de memória livre.
     */
    public void compactMemory();

    /**
     * Traduz um endereço lógico de um processo para um endereço físico.
     * 
     * @param processId Identificação do processo
     * @param logicalAddress Endereço lógico que será traduzido
     * @return Valor inteiro que representa a tradução do endereço lógico em endereço físico ou -1 caso não exista.
     * @throws InvalidAddress
     */
    public int getPhysicalAddress(int processId, int logicalAddress) throws InvalidAddress;

    /**
     * Processa um arquivo texto contendo um conjunto de comandos a serem executados.
     * 
     * @param fileName Path para o arquivo que será carregado.
     * @return Retorna true se o arquivo foi lido e seus comandos executados ou false caso contrário.
     */
    public boolean processCommandFile(String fileName);
}