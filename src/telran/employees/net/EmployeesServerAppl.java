package telran.employees.net;

import java.util.Scanner;

import telran.employees.services.EmployeesMethods;
import telran.employees.services.EmployeesMethodsMapsImpl;
import telran.net.TcpServer;

public class EmployeesServerAppl {   
private static final int TIMEOUT = 1000;

public static void main(String[] args) throws Exception{
	EmployeesMethods employees = new EmployeesMethodsMapsImpl("employees.data");
	employees.restore();
	TcpServer server = new TcpServer(2000, new EmployeesProtocol(employees));
	Thread thread = new Thread(server);
	thread.start();
	Scanner scanner = new Scanner(System.in);
	while (true) {
		System.out.println("For performing shutdown, type command shutdown");
		String line = scanner.nextLine();
		if (line.equals("shutdown")) {
			server.shutdown(TIMEOUT);
			break;
		}
	}
	Thread.sleep(TIMEOUT);
	employees.save();
	
}
}
