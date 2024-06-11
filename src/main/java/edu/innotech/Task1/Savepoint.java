package edu.innotech.Task1;

import edu.innotech.Task1.Account;
import edu.innotech.Task1.ECurrency;
import edu.innotech.Task1.Loadable;
import lombok.Getter;

import java.util.Map;

class Savepoint implements Loadable {
    @Getter private final String ownerName;
    @Getter private final Map<ECurrency, Integer> balance;
    private final Account savedAccount;

    public Savepoint(Account account){
        this.ownerName = account.getOwnerName();
        this.balance = account.getBalance();
        this.savedAccount = account;
    }

    @Override
    public void load(){
        savedAccount.load(this);
    }

    @Override
    public String toString() {
        String shortBalance = Account.balanceToStringViaBuilder(this.balance);
        return "Savepoint{ownerName='" + ownerName + '\'' +
                ", balance=" + shortBalance +
                '}';
    }
}
