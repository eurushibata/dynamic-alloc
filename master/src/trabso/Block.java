/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabso;

/**
 * Estrutura de dados do bloco
 * @author Yuji
 */
public class Block implements Comparable<Block> {
    private int process;
    private int base;
    private int size;
    
    
    /**
     * Método Construtor
     * @param process
     * @param base
     * @param size
     */
    public Block(int process, int base, int size) {
        this.process = process;
        this.base = base;
        this.size = size;
    }
    
    /**
     * Retorna a identificação do processo.
     * 
     * @return
     */
    public int getProcess(){
        return process;
    }

    /**
     * Retorna a base de um processo.
     * 
     * @return
     */
    public int getBase(){
        return base;
    }
    
    /**
     * Retorna o tamanho de um processo.
     * 
     * @return
     */
    public int getSize(){
        return size;
    }
    
    /**
     * Muda o valor de identificação de um processo.
     * 
     * @param process
     */
    public void setProcess(int process){
        this.process = process;
    }
    
    /**
     * Muda o valor de base de um processo.
     * 
     * @param base
     */
    public void setBase(int base){
        this.base = base;
    }
    
    /**
     * Muda o valor de tamanho de um processo.
     * 
     * @param size
     */
    public void setSize(int size){
        this.size = size;
    }
    
    /**
     * Compara a base de dois blocos de memória.
     * 
     * @param cBlock
     * @return 
     */
    public int compareTo(Block cBlock) {
        if (this.base < cBlock.base) {
            return -1;
        }
        if (this.base > cBlock.base) {
            return 1;
        }
        return 0;
    }
    
}
