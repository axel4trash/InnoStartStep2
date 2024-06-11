package edu.innotech.Task1;

import java.util.*;

public class Account {
    private String ownerName;
    private Map<ECurrency, Integer> balance = new HashMap<>();
    private final List<Savepoint> accountHistoryState = new ArrayList<>(); //История состояний объекта
    private final Stack<Command> commands = new Stack<>(); //список команд над Account в виде стека

    public Map<ECurrency, Integer>  getBalance() {
        return new HashMap<>(balance);
    }
    public Integer                  getBalanceByCurrency(ECurrency currency){
        return this.balance.get(currency);
    }
    public String                   getOwnerName() {
        return ownerName;
    }
    public List<Savepoint>          getAccountHistoryState() {return new ArrayList<>(this.accountHistoryState);}

    public void setOwnerName(String ownerName) {
        Command command = new CommandChangeOwnerName(ownerName, this);
        commands.push(command); //положили команду в стек
        command.execute();      //выполнили
    }

    public Account(String ownerName) {
        //Имя не может быть null или пустым
        if (ownerName == null|ownerName.isEmpty()|ownerName.isBlank()) throw new IllegalArgumentException ("Наименование владельца счета не может быть пустым!");
        this.ownerName = ownerName;
    }

    public void addToBalance(ECurrency currency, Integer amount){
        if (amount < 0) throw new IllegalArgumentException ("Количество не может быть отрицательным!");
        if (amount == null) throw new IllegalArgumentException ("Количество обязательно к заполнению!");

        //заменяет текущее количество данной Валюты на указанное.
        //Если такой валюты ранее не было – она добавляется в список.
        Command command = new CommandChangeBalance(currency, amount);
        commands.push(command); //положили команду в стек
        command.execute();      //выполнили
    }

    public void showBalance(){
        for (Map.Entry entry: this.balance.entrySet()) {
            System.out.println(entry);
        }
    }

    public static String balanceToStringViaBuilder(Map<ECurrency, Integer> p_balance){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        p_balance.forEach((key, value) -> {
            sb.append(key.getCode())
                    .append("=")
                    .append(value)
                    .append(", ");
        });
        sb.setLength(sb.length() - 2);
        sb.append("}");

        String result = sb.toString();

        return result;
    }

    @Override
    public String toString() {
        String shortBalance = Account.balanceToStringViaBuilder(this.getBalance());
        return "Account{" +
                "ownerName='" + ownerName + '\'' +
                ", balance=" + shortBalance +
                '}';
    }



    //>> История состояний объекта
    public Loadable save(){
        Savepoint savepoint = new Savepoint(this);
        this.accountHistoryState.add(savepoint);
        return savepoint;
    }

    public void load(Loadable save){
        Savepoint savepoint = (Savepoint)save;
        this.setOwnerName(savepoint.getOwnerName());
        this.balance.clear();
        this.balance.putAll(savepoint.getBalance());
        this.commands.clear(); //очистка истории команд
    }

    public void showAllHistoryState() {
        System.out.println(".showAllHistoryState: ownerName=" + this.ownerName);
        for (int i = 0; i < this.accountHistoryState.size(); i++) {
            System.out.println(" "+i+") " + this.accountHistoryState.get(i));
        }
    }
    //<< История состояний объекта

    public boolean canUndo(){
        return !this.commands.isEmpty();
    }

    public void showCommands(){
        for (Command command: this.commands) {
            System.out.println("command="+command);
        }
    }

    //отмена последних изменений
    public void undo(){
        System.out.println("Account.undo:");
        if (!canUndo()) {throw new NothingToUndo("Невозможно выполнить откат!");}
        commands.pop().undo();
    }

    //Реализация паттерна Command на внутренних классах для доступа к полям Account
    public class CommandChangeOwnerName implements Command {
        private String newOwnerName;
        private String prevOwnerName;

        public CommandChangeOwnerName(String newOwnerName, Account account) {
            this.newOwnerName = newOwnerName;
        }

        @Override
        public void execute() {
            System.out.println(".CommandChangeOwnerName.execute: ");
            this.prevOwnerName = Account.this.ownerName;
            Account.this.ownerName = this.newOwnerName;
        }

        @Override
        public void undo() {
            System.out.println(".CommandChangeOwnerName.undo: ");
            Account.this.ownerName = this.prevOwnerName;
        }
    }

    public class CommandChangeBalance implements Command {
        ECurrency newCurrency;
        ECurrency prevCurrency;
        Integer newAmount;
        Integer prevAmount;
        boolean delCurrency;

        public CommandChangeBalance(ECurrency currency, Integer amount) {
            this.newCurrency = currency;
            this.newAmount = amount;

            if (Account.this.balance.containsKey(currency)){ //если валюта уже была, то просто изменение суммы, иначе - добавление новой валюты
                this.delCurrency = false;
                this.prevAmount = Account.this.getBalanceByCurrency(currency);
                this.prevCurrency = currency;
            }
            else {
                this.delCurrency = true;

            }
        }

        @Override
        public void execute() {
            System.out.println(".CommandChangeBalance.execute: ");
            Account.this.balance.put(this.newCurrency, this.newAmount);
            System.out.println(".CommandChangeBalance.execute: NEW currency="+this.newCurrency.getCode()+ " amount="+this.newAmount);
        }

        @Override
        public void undo() {
            System.out.println(".CommandChangeBalance.undo: delCurrency="+this.delCurrency);

            if (this.delCurrency){
                Account.this.balance.remove(this.prevCurrency);
            }else
            {
                System.out.println(".CommandChangeBalance.execute: put="+this.prevCurrency.getCode()+ " amount="+this.prevAmount);
                Account.this.balance.put(this.prevCurrency, this.prevAmount);
            }
        }
    }
}
