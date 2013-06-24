package trabso;

import exceptions.MemoryOverflow;
import exceptions.InvalidAddress;
import interfaces.ManagementInterface;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Yuji
 */
public class ContiguousAllocationManager implements ManagementInterface {
    private int memorySize;
    /**
     * 
     */
    public ArrayList<Block> dynamicMemory;
    private int clast;
    private Boolean policy;
    private int[][] memoryBlocks;
    SimulationUser su = new SimulationUser();
    
    /**
     * Construtor da classe.
     * 
     * @param memorySize Tamanho total do bloco de memória.
     */
    public ContiguousAllocationManager(int memorySize) {
        this.memorySize = memorySize;
        dynamicMemory = new ArrayList<Block>(this.memorySize);
        clast = 0;
        policy=true;
        freeAll();
    }
    
    /**
     * Retorna true se a política escolhida for Next-Fit ou false se a política escolhida for Worst-Fit.
     * 
     * @param policy true se a política utilizada é Next-Fit ou false se for Worst-Fit.
     */
    public void setPolicy(int policy) {
        if (policy == 0) {
            this.policy = true; 
        } else if (policy == 1) {
            this.policy = false;
        }
    }
    
    /**
     * Retorna o tipo de política escolhida.
     * 
     * @return policy true se a política utilizada é Next-Fit ou false se for Worst-Fit. 
     */
    public boolean getPolicy() {
        return this.policy;
    }

    /**
     * Aloca um bloco de memória para um processo de acordo com uma política de localização de blocos livres.
     * 
     * @param processId Identificação do processo que será alocado.
     * @param size Tamanho do Processo alocado.
     * @return Retorna true se o processo foi alocado com sucesso ou false se o processo não pode ser alocado.
     * @throws MemoryOverflow
     */
    public boolean allocateMemoryBlock(int processId, int size) throws MemoryOverflow{
        if(checkConsistency(processId)){
            int i=0, max=dynamicMemory.size(), cindex=-1;

            // next-fit
            try {
                if(policy){
                    for(i=clast; i<max; i++){
                        if((dynamicMemory.get(i).getProcess() == -1) && (dynamicMemory.get(i).getSize()>=size)){
                            clast = i;
                            cindex = clast;
                            break;
                        }
                    }
                    if(cindex==-1){
                        for(i=0; i<clast; i++){
                            if((dynamicMemory.get(i).getProcess() == -1) && (dynamicMemory.get(i).getSize()>=size)){
                                clast = i;
                                cindex = clast;
                                break;
                            }
                        }
                    }
                }
            // worst-fit
                else if(!policy){
                    cindex = -1;
                    for(i=0; i<dynamicMemory.size(); i++) {
                        // Buscar por todos os blocos sem processo e que cabe o novo processo
                        if ((dynamicMemory.get(i).getProcess() == -1)&&(dynamicMemory.get(i).getSize()>=size)) {                 
                            // Bloco candidato a alocar o processo
                            if (cindex == -1) {
                                cindex = i;
                            } else {
                                if (dynamicMemory.get(cindex).getSize() < dynamicMemory.get(i).getSize()) {
                                    cindex = i;
                                }
                            }
                        }

                    }
                }

    //        cindex < total dos -1
    //         só retorna falso

                // alocação do novo bloco
                if (cindex == -1) {

                    System.out.println("@@@");
                    throw new MemoryOverflow();
                } else {
                // alocar novo processo e fragmentar o bloco se necessário
                    Block curr = dynamicMemory.get(cindex);
                    dynamicMemory.add(new Block(processId, curr.getBase(), size));
                    if ((curr.getSize()-size)>0) {
                        dynamicMemory.add(new Block(-1, curr.getBase()+size, curr.getSize()-size));
                    }
                    dynamicMemory.remove(cindex);
                    Collections.sort(dynamicMemory);

                    fillMemoryMap();
                    su.displayMemoryMap(memoryBlocks);

                    return true;
                }
            } catch(MemoryOverflow mo) {
                System.out.println("erro estouro de memoria");
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Libera um bloco de memória ocupado por um processo.
     * 
     * @param processId Identificação do processo que será removido.
     * @return Retorna true se um bloco de memória foi removido com sucesso ou false se o bloco não pode ser removido.
     */
    public boolean freeMemoryBlock(int processId) {
        int cindex = -1;
        for(int i=0; i<dynamicMemory.size(); i++) {
            if (dynamicMemory.get(i).getProcess() == processId) {
                cindex = i;
            }
        }

        if (cindex == -1) {
            fillMemoryMap();
            su.displayMemoryMap(memoryBlocks);
            return false;
        } else {
            Block curr = dynamicMemory.get(cindex);
            dynamicMemory.add(new Block(-1, curr.getBase(), curr.getSize()));
            dynamicMemory.remove(curr);
            Collections.sort(dynamicMemory);

            // Merge dos blocos vazios
            for(int k=0; k<2;k++) { // Executa 2x para garantir merge no caso de 3 blocos, pois o merge executa em grupamentos de 2 blocos
                if (dynamicMemory.size() > 1) {
                    for(int i=0; i<dynamicMemory.size()-1; i++) {
                        if ((dynamicMemory.get(i).getProcess() == -1)&&(dynamicMemory.get(i+1).getProcess() == -1)) {
                            //merge
                            int base = dynamicMemory.get(i).getBase();
                            int size = dynamicMemory.get(i).getSize() + dynamicMemory.get(i+1).getSize();
                            dynamicMemory.add(new Block(-1, base, size));
                            dynamicMemory.remove(i+1);
                            dynamicMemory.remove(i);
                            i = dynamicMemory.size();
                            Collections.sort(dynamicMemory);
                        }
                    }
                }
            }
            
            fillMemoryMap();
            su.displayMemoryMap(memoryBlocks);
            return true;
        }
    }

    /**
     * Libera todos os blocos de memória ocupados.
     */
    public void freeAll(){
        dynamicMemory.clear();
        Block block = new Block(-1, 0, memorySize);
        dynamicMemory.add(block);
        fillMemoryMap();
        su.displayMemoryMap(memoryBlocks);
    }

    /**
     * Redistribui o conteúdo da memória de modo a criar um grande e único bloco de memória livre.
     */
    public void compactMemory(){
        Collections.sort(dynamicMemory);
        // Memória auxiliar
        ArrayList<Block> dynamicMemoryAux = new  ArrayList<Block>(dynamicMemory);
        
        freeAll();

        for(int i=0; i<dynamicMemoryAux.size(); i++) {
            if (dynamicMemoryAux.get(i).getProcess() != -1) {
                try {
                    allocateMemoryBlock(dynamicMemoryAux.get(i).getProcess(), dynamicMemoryAux.get(i).getSize());
                } catch (MemoryOverflow ex) {
                    Logger.getLogger(ContiguousAllocationManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        fillMemoryMap();
        su.displayMemoryMap(memoryBlocks);
    }

    /**
     * Traduz um endereço lógico de um processo para um endereço físico.
     * 
     * @param processId Identificação do processo
     * @param logicalAddress Endereço lógico que será traduzido
     * @return Valor inteiro que representa a tradução do endereço lógico em endereço físico ou -1 caso não exista.
     * @throws InvalidAddress
     */
    public int getPhysicalAddress(int processId, int logicalAddress) throws InvalidAddress{
        int i=0, physicalAddress=-1, finalAddress;
        Boolean flag=false;
        try{
            if(logicalAddress < 0 || processId < 0){
                throw new InvalidAddress();
            }
            while(i<dynamicMemory.size() && flag==false){
            //for(i=0; i<dynamicMemory.size(); i++){
                if(dynamicMemory.get(i).getProcess() == processId){
                    finalAddress = dynamicMemory.get(i).getBase() + dynamicMemory.get(i).getSize();
                    physicalAddress = dynamicMemory.get(i).getBase() + logicalAddress;
                    if(physicalAddress > finalAddress){
                        throw new InvalidAddress();
                    }else{
                        flag = true;
                    }
                }
                i++;
            }
        }catch(InvalidAddress ia){
            System.err.println("erro: " + ia.getMessage());
            return -1;
        }
        return physicalAddress;
    }

    /**
     * Processa um arquivo texto contendo um conjunto de comandos a serem executados.
     * 
     * @param fileName Path para o arquivo que será carregado.
     * @return Retorna true se o arquivo foi lido e seus comandos executados ou false caso contrário.
     */
    public boolean processCommandFile(String fileName){
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            String[] command = new String[100];
            while (in.ready()) {
                str = in.readLine();
                command = str.split(" ");
                if(command[0].equals("allocateBlock")){
                    if(allocateMemoryBlock(Integer.parseInt(command[1]), Integer.parseInt(command[2]))){
                        System.out.println("Processo Inserido!");
                    }
                    } else if(command[0].equals("freeBlock")){
                        if(freeMemoryBlock(Integer.parseInt(command[1]))){
                            System.out.println("Processo Removido!");
                        }
                    } else if(command[0].equals("freeAll")){
                        freeAll();
                    } else if(command[0].equals("compact")){
                        compactMemory();
                }
            }
            in.close();
            return true;
            } catch (IOException e) {
                }
                catch (MemoryOverflow mo){
                    
                }

        return false;

    }
    
    /**
     * Preenche uma matriz com os seguintes atributos: Processo, Base e Tamanho.
     * 
     */
    public void fillMemoryMap(){
        memoryBlocks = new int[dynamicMemory.size()][3];
        for(int i=0; i<dynamicMemory.size(); i++){
            memoryBlocks[i][0] = dynamicMemory.get(i).getProcess();
            memoryBlocks[i][1] = dynamicMemory.get(i).getBase();
            memoryBlocks[i][2] = dynamicMemory.get(i).getSize();
        }
    }
    
    /**
     * Verifica a consistência dos processos alocados durante a execução.
     * 
     * @param processId Identificação do processo a ser verificado.
     * @return Retorna true se não há processos com mesmo identificação sendo alocados ou se a identificação -1 (vazio)
     * está sendo alocada ou false se é encontrada alguma inconsistência.
     */
    public Boolean checkConsistency(int processId){
        if(processId == -1){
            return false;
        }
        for(int i=0; i<dynamicMemory.size(); i++){
            if(dynamicMemory.get(i).getProcess() == processId){
                return false;
            }
        }
        return true;
    }
}