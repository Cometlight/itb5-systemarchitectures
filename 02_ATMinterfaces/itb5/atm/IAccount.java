package itb5.atm;

public interface IAccount {
    public void deposit(int accountNo, float amount);
    public void withdraw(int accountNo, float amount);
    public float getBalance(int accountNo);
}
