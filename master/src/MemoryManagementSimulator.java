
import exceptions.MemoryOverflow;
import trabso.ContiguousAllocationManager;
import exceptions.InvalidAddress;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Yuji
 */
public class MemoryManagementSimulator {

    /**
     * @param args the command line arguments
     * @throws MemoryOverflow 
     * @throws InvalidAddress  
     */
    public static void main(String[] args) throws MemoryOverflow, InvalidAddress {
        if (args.length < 2) {
            usage();
            System.exit(0);
        }
        
        int memorySize = 0, policy = 0;
        
        try {
            memorySize = Integer.parseInt(args[0]);
            policy = Integer.parseInt(args[1]);
            if (policy!=0 && policy!=1) {
                usage();
                System.exit(0);
            }
        } catch(NumberFormatException e) {
            usage();
            System.exit(0);
        }
        
        ContiguousAllocationManager m = new ContiguousAllocationManager(memorySize);
        m.setPolicy(policy);
        
        Scanner sc = new Scanner(System.in);
        String[] op = new String[100];
            
        while(true) {
            try {
                menu();
                op = sc.nextLine().split(" ");

                if(op[0].equals("allocateBlock")) {
                    if(m.allocateMemoryBlock(Integer.parseInt(op[1]), Integer.parseInt(op[2]))){
                        System.out.println("Processo Inserido!");
                    }
                } else if(op[0].equals("freeBlock")){
                    if(m.freeMemoryBlock(Integer.parseInt(op[1]))){
                        System.out.println("Processo Removido!");
                    }
                } else if(op[0].equals("freeAll")){
                    m.freeAll();
                } else if(op[0].equals("compact")){
                    m.compactMemory();
                } else if(op[0].equals("physicalAddress")) {
                    System.out.println("Endereço Físico:"+m.getPhysicalAddress(Integer.parseInt(op[1]), Integer.parseInt(op[2])));
                } else if(op[0].equals("processFile")){
                    System.out.println("Processando arquivo...");
                    m.processCommandFile(op[1]);
                } else if(op[0].equals("exit")){
                    System.exit(0);
                } else {
                    System.err.println("COMANDO INVÁLIDO");
                }

                for(int i=0; i<m.dynamicMemory.size(); i++) {
                    System.out.println(m.dynamicMemory.get(i).getProcess() + "/" + m.dynamicMemory.get(i).getBase()+"/"+m.dynamicMemory.get(i).getSize());
                }
             
            } catch (NumberFormatException e) {
                System.err.println("VALOR INVÁLIDO");
            }
        }
    
    } 
    /**
     * Método que imprime ao usuário os possíveis erros ocorridos durante uma solicitação.
     */
    public static void usage() {
        
        System.err.println("Wrong or missing arguments.");
        System.err.println("Usage: MemoryManagementSimulator.jar <memory_size> <policy> [<files>].");
        System.err.println("<memory_size>: integer");
        System.err.println("<policy>: 0 Next-fit | 1 Worst-fit");
        System.err.println("[<files>]: files");
    }
    
    /**
     * Imprime o menu do sistema.
     */
    public static void menu() {
        System.out.println("\n\n  Comandos:");
        System.out.println("    allocateBlock <process_id> <size>");
        System.out.println("    freeBlock <process_id>");
        System.out.println("    freeAll");
        System.out.println("    compact");
        System.out.println("    physicalAddress <process_id> <logical_address>");
        System.out.println("    processFile <fileName>");
        System.out.println("    exit");
        System.out.println("----------------------------------------------------");
        System.out.print(" > ");
        
    }

}
