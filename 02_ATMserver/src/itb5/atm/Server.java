package itb5.atm;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
	public static void main(String[] args) {
		IATMFactory atmfactory = null;
		try {
			atmfactory = new ATMFactoryImpl();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		try {
			Naming.rebind("rmi://localhost:1337/atmfactory", atmfactory);
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
