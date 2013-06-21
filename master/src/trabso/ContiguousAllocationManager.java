package trabso;

import exceptions.MemoryOverflow;
import exceptions.InvalidAddress;
import interfaces.ManagementInterface;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContiguousAllocationManager implements ManagementInterface {
    private int memorySize;
    public ArrayList<Block> dynamicMemory;
    
    public ContiguousAllocationManager(int memorySize) {
        this.memorySize = memorySize;
        dynamicMemory = new ArrayList<Block>(this.memorySize);
        freeAll();
    }

    // aloca um bloco de memoria para um processo 
    public boolean allocateMemoryBlock(int processId, int size) {
    // next-fit
        
        // worst-fit
        int cindex = -1;
        for(int i=0; i<dynamicMemory.size(); i++) {
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
	public boolean freeMemoryBlock(int processId){
            int i=0, j, newBase, newSize;
            Boolean flag=false;
            for(i=0; i<dynamicMemory.size(); i++){
                if(dynamicMemory.get(i).getProcess() == processId){
                    try {
                        //faz a remoçao do bloco de memoria caso os blocos acima e abaixo estejam livres
                        if(dynamicMemory.get(i).getBase() != 0 && dynamicMemory.get(i-1).getProcess() == -1 
                            && getPhysicalAddress(i, dynamicMemory.get(i).getSize()) != memorySize && dynamicMemory.get(i+1).getProcess() == -1){
                            
                            newBase = dynamicMemory.get(i-1).getBase();
                            newSize = getPhysicalAddress(i+1, dynamicMemory.get(i+1).getSize()) - newBase;
                            dynamicMemory.get(i-1).setBase(newBase);
                            dynamicMemory.get(i-1).setSize(newSize);
                            dynamicMemory.remove(i);
                            dynamicMemory.remove(i+1);
                            
                            //compactando o array de modo que nao fiquem espaços vazios
                            j=i;
                            while((j+2) < dynamicMemory.size()){
                                dynamicMemory.set(j, dynamicMemory.get(j+2));
                                j++;
                            }
                            dynamicMemory.set(j, dynamicMemory.get(j+1));
                            dynamicMemory.remove(j+1);
                            
                            flag = true;
                        }
                        //faz a remoçao do bloco de memoria caso apenas o bloco acima esteja livre
                        else if((dynamicMemory.get(i).getBase() != 0 && dynamicMemory.get(i-1).getProcess() == -1) 
                                && (getPhysicalAddress(i, dynamicMemory.get(i).getSize()) == memorySize || dynamicMemory.get(i+1).getProcess() != -1)){
                            
                                newBase = dynamicMemory.get(i-1).getBase();
                                newSize = getPhysicalAddress(i, dynamicMemory.get(i).getSize()) - newBase;
                                dynamicMemory.get(i-1).setBase(newBase);
                                dynamicMemory.get(i-1).setSize(newSize);
                                dynamicMemory.remove(i);
                                
                                //compactando o array de modo que nao fiquem espaços vazios
                                j=i;
                                while((j+1) < dynamicMemory.size()){
                                    dynamicMemory.set(j, dynamicMemory.get(j+1));
                                    j++;
                                }
                                dynamicMemory.remove(j);
                                
                                flag = true;
                        }
                        //faz a remoçao do bloco de memoria caso apenas o bloco abaixo esteja livre
                        else if((getPhysicalAddress(i, dynamicMemory.get(i).getSize()) != memorySize && dynamicMemory.get(i+1).getProcess() == -1)
                                && (dynamicMemory.get(i).getBase() == 0 || dynamicMemory.get(i-1).getProcess() != -1)){
                            
                                newSize = getPhysicalAddress(i+1, dynamicMemory.get(i+1).getSize()) - dynamicMemory.get(i).getBase();
                                dynamicMemory.get(i).setSize(newSize);
                                dynamicMemory.remove(i+1);
                                
                                //compactando o array de modo que nao fiquem espaços vazios
                                j=i+1;
                                while((j+1) < dynamicMemory.size()){
                                    dynamicMemory.set(j, dynamicMemory.get(j+1));
                                    j++;
                                }
                                dynamicMemory.remove(j);
                                
                                flag = true;
                            
                        }
                        //faz a remoçao do bloco de memoria caso nao haja espaços livres adjacentes
                        else if((dynamicMemory.get(i).getBase() == 0 && dynamicMemory.get(i+1).getProcess() != -1)
                                || (getPhysicalAddress(i, dynamicMemory.get(i).getSize()) == memorySize && dynamicMemory.get(i-1).getProcess() != -1) 
                                || (dynamicMemory.get(i-1).getProcess() != -1 && dynamicMemory.get(i+1).getProcess() != -1)){
                            
                            dynamicMemory.get(i).setProcess(-1);
                            
                            flag = true;
                        }
                        
                        
                    } catch (InvalidAddress ia) {
                        System.err.println("erro: " + ia.getMessage());
                    }
                }
            }
            if(flag==true){
                return flag;
            }else
                return false;
            
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
            while(i<dynamicMemory.size() || flag==false){
            //for(i=0; i<dynamicMemory.size(); i++){
                if(dynamicMemory.get(i).getProcess() == processId){
                    try{
                        finalAddress = dynamicMemory.get(i).getBase() + dynamicMemory.get(i).getSize();
                        physicalAddress = dynamicMemory.get(i).getBase() + logicalAddress;
                        if(physicalAddress > finalAddress){
                            throw new InvalidAddress();
                        }else{
                            flag = true;
                        }
                    }catch(InvalidAddress ia){
                        System.err.println("erro: " + ia.getMessage());
                    }
                }
                i++;
            }
            return physicalAddress;
	}

	// processa um arquivo texto contendo um conjunto de comandos
	public boolean processCommandFile(String fileName){
            return false;
		
	}
}