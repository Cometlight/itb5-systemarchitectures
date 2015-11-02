package itb5.atm;

import itb5.atm.IATM;
import itb5.atm.IATMFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Program {
    public static void main(String[] args) {
        System.out.println("Hello ATM!");

        IATMFactory atmFactory;
        IATM atm = null;
		try {
			atmFactory = (IATMFactory)Naming.lookup("rmi://localhost:1337/atmfactory");
			atm = atmFactory.createATM();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
        

        // get initial account balance
        System.out.println("Initial Balances");
        System.out.println("Balance(1): "+atm.getBalance(1));
        System.out.println("Balance(2): "+atm.getBalance(2));
        System.out.println("Balance(3): "+atm.getBalance(3));
        System.out.println();
        // make €1000 depoist in account 1 and get new balance
        System.out.println("Depositting(1): 1000 ");
        atm.deposit(1, 1000);
        System.out.println("Balance(1): "+atm.getBalance(1));
        // make €100 withdrawal from account 2 and get new balance
        System.out.println("Withdrawing(2): 100 ");
        atm.withdraw(2, 100);
        System.out.println("Balance(2): "+atm.getBalance(2));
        // make €500 deposit in account 3 and get new balance
        System.out.println("Depositting(3): 500 ");
        atm.deposit(3, 500);
        System.out.println("Balance(3): "+atm.getBalance(3));
        // get final account balance
        System.out.println();
        System.out.println("Final Balances");
        System.out.println("Balance(1): "+atm.getBalance(1));
        System.out.println("Balance(2): "+atm.getBalance(2));
        System.out.println("Balance(3): "+atm.getBalance(3));
    }
}
