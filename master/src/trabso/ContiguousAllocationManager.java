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
    private int cindex;
    private Boolean policy;
    
    public ContiguousAllocationManager(int memorySize) {
        this.memorySize = memorySize;
        dynamicMemory = new ArrayList<Block>(this.memorySize);
        cindex = 0;
        policy=true;
        freeAll();
    }

    // aloca um bloco de memoria para um processo 
    public boolean allocateMemoryBlock(int processId, int size) {
        int i=0, max=dynamicMemory.size();
        
        // next-fit
        if(policy){
            Boolean flag=false;
            while(flag==false){
                if(i==max){
                    i=0;
                }
                if((dynamicMemory.get(i).getProcess() == -1) && (dynamicMemory.get(i).getSize()>=size)){
                    cindex = i;
                    flag=true;
                }
            i++;
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
        
        // alocação do novo bloco
        if (cindex == -1) {
//            throw new MemoryOverflow();
            System.out.println("Não há memória disponível");
            return false;
        } else {
        // alocar novo processo e fragmentar o bloco se necessário
            Block curr = dynamicMemory.get(cindex);
            dynamicMemory.add(new Block(processId, curr.getBase(), size));
            if ((curr.getSize()-size)>0) {
                dynamicMemory.add(new Block(-1, curr.getBase()+size, curr.getSize()-size));
            }
            dynamicMemory.remove(cindex);
            Collections.sort(dynamicMemory);
            return true;
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
                
                return true;
            }
            
            
                
             
//            Block curr = dynamicMemory.get(processId);
//            
//            dynamicMemory.add(new Block(-1, curr.getBase(), curr.getSize()));
//            dynamicMemory.remove(curr);
	}
	
	// libera todos os blocos de memoria ocupados
	public void freeAll(){
        dynamicMemory.clear();
        Block block = new Block(-1, 0, memorySize);
        dynamicMemory.add(block);
	}

 	// redistribui o conteudo da memoria de modo a criar um grande e unico bloco de memoria livre
	public void compactMemory(){
            
	}
	
	// traduz um endereco logico de um processo para um endereco fisico
	public int getPhysicalAddress(int processId, int logicalAddress) throws InvalidAddress{
            int i=0, physicalAddress=0, finalAddress;
            Boolean flag=false;
            try{
                if(logicalAddress < 0 || processId < 0){
                    throw new InvalidAddress();
                }
                while(i<dynamicMemory.size() || flag==false){
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

            return false;
		
	}
}