
public class Main {
	public static void main(String[] args) {
		TraceFileReader reader = new TraceFileReader();
	    ActiveList aclist = new ActiveList();
	    Logger logger = new Logger();
	    int MAX_COUNT = 1;
	    Committer committer = new Committer(logger, aclist, MAX_COUNT, 'C');
	    RegisterManager reg_mgr = new RegisterManager(logger, committer, 'W');
	    InstructionQueue fp_queue = new InstructionQueue(logger, reg_mgr);
	    InstructionQueue integer_queue = new InstructionQueue(logger, reg_mgr);
	    InstructionQueue addr_queue = new InstructionQueue(logger, reg_mgr);
	    FPALU fp_add = new FPALU(logger, fp_queue, reg_mgr, ALU.ALUType.ADD);
	    FPALU fp_mul = new FPALU(logger, fp_queue, reg_mgr, ALU.ALUType.MULTIPLY);
	    IntALU int_alu = new IntALU(logger, integer_queue, reg_mgr);
	    AddrALU addr_alu = new AddrALU(logger,  addr_queue, reg_mgr);
	    Decoder decoder = new Decoder(logger, fp_queue, addr_queue, integer_queue,
	                  aclist, reg_mgr, MAX_COUNT, 'D');
	    Fetcher fetcher = new Fetcher(logger, reader, decoder, MAX_COUNT, 'F');
		try {
			reader.load("test1.trace");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
	    do {
	        committer.calc();
	        reg_mgr.calc();
	        fp_add.calc();
	        fp_mul.calc();
	        int_alu.calc();
	        addr_alu.calc();
	        fp_queue.calc();
	        integer_queue.calc();
	        addr_queue.calc();
	        decoder.calc();
	        decoder.calc();
	        fetcher.calc();
	        
	        fetcher.edge();
	        decoder.edge();
	        decoder.edge();
	        fp_queue.edge();
	        integer_queue.edge();
	        addr_queue.edge();
	        fp_add.edge();
	        fp_mul.edge();
	        int_alu.edge();
	        addr_alu.edge();
	        reg_mgr.edge();
	        committer.edge();
	        logger.increment();
	        i++;
	    } while ( !committer.isEmpty() ||
	              !fp_add.isEmpty() ||
	              !fp_mul.isEmpty() ||
	              !int_alu.isEmpty() ||
	              !addr_alu.isEmpty() ||
	              !fp_queue.isEmpty() ||
	  	          !integer_queue.isEmpty() || 
	  	          !addr_queue.isEmpty() ||
	              !decoder.isEmpty() ||
	              !fetcher.isEmpty());
	    logger.print();
	}
}
