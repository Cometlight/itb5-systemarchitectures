package itb5.atm;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class ATMImpl extends UnicastRemoteObject implements IATM {
	private static final long serialVersionUID = 1L;
	private Map<Integer, Account> _accounts;

	public ATMImpl() throws RemoteException {
		Account acc1 = new Account(0);
		Account acc2 = new Account(100);
		Account acc3 = new Account(500);
		
		_accounts.put(1, acc1);
		_accounts.put(2, acc2);
		_accounts.put(3, acc3);
	}
	
	@Override
	public void deposit(int accountNo, float amount) {
		Account account = _accounts.get(accountNo);
		if(account != null) {
			account.deposit(amount);
		}
	}

	@Override
	public void withdraw(int accountNo, float amount) {
		Account account = _accounts.get(accountNo);
		if(account != null) {
			account.withdraw(amount);
		}
	}

	@Override
	public float getBalance(int accountNo) {
		Account account = _accounts.get(accountNo);
		if(account != null) {
			return account.getBalance(accountNo);
		} else {
			return 0;
		}
	}
	
}
