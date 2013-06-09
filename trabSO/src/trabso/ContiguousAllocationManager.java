package trabSO;

import java.util.*;
import trabso.ListNode;

public class ContiguousAllocationManager implements ManagementInterface {
	private int memorySize;
        ArrayList<ListNode> dynamicMemory;

	public ContiguousAllocationManager(int memorySize) {
            this.memorySize = memorySize;
            dynamicMemory = new ArrayList(this.memorySize);
	}

	// aloca um bloco de memoria para um processo 
	public boolean allocateMemoryBlock(int processId, int size) throws MemoryOverflow {
            
	}

	// libera um bloco de memoria ocupado por um processo
	public boolean freeMemoryBlock(int processId){
            
	}
	
	// libera todos os blocos de memoria ocupados
	public void freeAll(){
            dynamicMemory.clear();
            ListNode node = new ListNode(-1, 0, memorySize);
            dynamicMemory.add(node);
            
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
            }
            return physicalAddress;
	}

	// processa um arquivo texto contendo um conjunto de comandos
	public boolean processCommandFile(String fileName){
		
	}
}