import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Main {
	public static void main(String[] args) {
		TraceFileReader reader = new TraceFileReader();
	    ActiveList aclist = new ActiveList();
	    Logger logger = new Logger();
	    int MAX_COUNT = 1;
	    int COMMIT_COUNT = 4;
	    Committer committer = new Committer(logger, aclist, COMMIT_COUNT, 'C');
	    RegisterManager reg_mgr = new RegisterManager(logger, committer, 'W');
	    InstructionQueue fp_queue = new InstructionQueue(logger, reg_mgr);
	    InstructionQueue integer_queue = new InstructionQueue(logger, reg_mgr);
	    AddressQueue addr_queue = new AddressQueue(logger, reg_mgr);
	    committer.setAddressQueue(addr_queue);
	    committer.setRegisterManager(reg_mgr);
	    MemLoader memloader = new MemLoader(logger, addr_queue,reg_mgr, 'M');
	    FPALU fp_add = new FPALU(logger, fp_queue, reg_mgr, ALU.ALUType.ADD);
	    FPALU fp_mul = new FPALU(logger, fp_queue, reg_mgr, ALU.ALUType.MULTIPLY);
	    IntALU int_alu = new IntALU(logger, integer_queue, reg_mgr,ALU.ALUType.ADD);
	    AddrALU addr_alu = new AddrALU(logger,  addr_queue, reg_mgr, 'A');
	    Issuer issuer = new Issuer(logger, fp_queue, addr_queue, integer_queue,
	                  aclist, reg_mgr, MAX_COUNT, 'I');
	    Widget decoder = new Widget(logger, issuer, MAX_COUNT, 'D');
	    Fetcher fetcher = new Fetcher(logger, reader, decoder, MAX_COUNT, 'F');
	    
	    List<Widget> components = new ArrayList<Widget>();
	    components.add(fetcher);
	    components.add(decoder);
	    components.add(issuer);
	    components.add(fp_add);
	    components.add(fp_mul);
	    components.add(int_alu);
	    components.add(addr_alu);
	    components.add(memloader);
	    components.add(reg_mgr);
	    components.add(committer);
		try {
			reader.load("test1.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int j = 0;
	    while(true) {
	    	if (j== 3) {
	        	System.out.println();
	    	}
	        for (int i=components.size()-1; i>=0; i--)
	        	components.get(i).calc();
	        for (int i=0; i<components.size(); i++) 
	        	components.get(i).edge();

	        boolean proceed = false;
	        for (int i=0; i<components.size(); i++) {
	        	if (!components.get(i).isEmpty()) {
	        		proceed = true; 
	        		System.out.println(i);
	        	}
	        }
	        if (!proceed) break;
	        logger.increment();
	        j++;
	    } 
	    try {
			logger.print(new PrintWriter("output.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
