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

public class ContiguousAllocationManager implements ManagementInterface {
    private int memorySize;
    public ArrayList<Block> dynamicMemory;
    private int clast;
    private Boolean policy;
    private int[][] memoryBlocks;
    SimulationUser su = new SimulationUser();
    
    public ContiguousAllocationManager(int memorySize) {
        this.memorySize = memorySize;
        dynamicMemory = new ArrayList<Block>(this.memorySize);
        clast = 0;
        policy=true;
        freeAll();
    }
    
    public void setPolicy(int policy) {
        if (policy == 0) {
            this.policy = true; 
        } else if (policy == 1) {
            this.policy = false;
        }
        // true = Next-fit
        // false = Worst-fit
    }
    
    public boolean getPolicy() {
        return this.policy;
    }

    // aloca um bloco de memoria para um processo 
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

    // libera um bloco de memoria ocupado por um processo
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

    // libera todos os blocos de memoria ocupados
    public void freeAll(){
        dynamicMemory.clear();
        Block block = new Block(-1, 0, memorySize);
        dynamicMemory.add(block);
        fillMemoryMap();
        su.displayMemoryMap(memoryBlocks);
    }

    // redistribui o conteudo da memoria de modo a criar um grande e unico bloco de memoria livre
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

    // traduz um endereco logico de um processo para um endereco fisico
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

    // processa um arquivo texto contendo um conjunto de comandos
    public boolean processCommandFile(String fileName){
        System.out.println(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            System.out.println("Processando arquivo...");
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
                System.err.println("Arquivo inválido");
            } catch (MemoryOverflow mo){
                    
                }

        return false;

    }
    
    public void fillMemoryMap(){
        memoryBlocks = new int[dynamicMemory.size()][3];
        for(int i=0; i<dynamicMemory.size(); i++){
            memoryBlocks[i][0] = dynamicMemory.get(i).getProcess();
            memoryBlocks[i][1] = dynamicMemory.get(i).getBase();
            memoryBlocks[i][2] = dynamicMemory.get(i).getSize();
        }
    }
    
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