package edu.innotech.Task1;

public class Main {
    public static void main(String[] args) {
        Account account1 = new Account("Alex");
        account1.addToBalance(ECurrency.RUB, 1000);
        account1.addToBalance(ECurrency.USD, 1000);

        System.out.println("Show account:");
        System.out.println(account1);
        System.out.println("Show balance:");
        account1.showBalance();
        account1.addToBalance(ECurrency.USD, 5000);
        account1.showBalance();
        System.out.println("USD="+account1.getBalanceByCurrency(ECurrency.USD));
        System.out.println("AED="+account1.getBalanceByCurrency(ECurrency.AED));
        System.out.println(account1.getBalance());

        account1.getBalance()
                .forEach((currency, amount) ->
                        System.out.println(currency.getCode() + " = " + amount));

        System.out.println("balanceToStringViaBuilder:" + Account.balanceToStringViaBuilder(account1.getBalance()));

        //изменение
        System.out.println("изменение: USD=4400");
        account1.addToBalance(ECurrency.USD, 4400);
        account1.showAllHistoryState();

        System.out.println(account1);

        System.out.println("---------------Часть 2 Отмена--------------");
        //account1.undo(); //new exception

        account1.setOwnerName("Bob");
        System.out.println(account1);
        account1.showCommands();
        account1.undo();
        //System.out.println(account1);
        //account1.undo();//Невозможно выполнить откат!
    }


}
