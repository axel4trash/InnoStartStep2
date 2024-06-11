package edu.innotech.Task1;

import edu.innotech.Task1.Account;
import edu.innotech.Task1.ECurrency;
import edu.innotech.Task1.NothingToUndo;
import edu.innotech.Task1.Loadable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AccountTests {

    @Test
    @DisplayName("Test 1: Имя не может быть null или пустым")
    public void ownerNameNotNull(){
        try {
            Account st = new Account(new String());
            System.out.println("Error");
            Assertions.fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            System.out.println("OK");
            Assertions.assertNotEquals("", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test 2: Имя не может быть пустым")
    public void ownerNameNotEmpty(){
        try {
            Account st = new Account("");
            System.out.println("Error");
            Assertions.fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            System.out.println("OK");
            Assertions.assertNotEquals("", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test 3: Количество валюты не может быть отрицательным")
    public void amountNotNegative(){
        Account acc = new Account("Bob");
        Assertions.assertThrows(IllegalArgumentException.class, ()->
                acc.addToBalance(ECurrency.USD, -100));
        System.out.println("OK");
    }

    // Тест для проверки установки положительного количества валюты
    @Test
    @DisplayName("Test 4: Добавление счета и изменение баланса")
    public void balanceSetCurrency(){
        Account acc = new Account("TestMan");
        ECurrency currency = ECurrency.RUB;
        try {
            acc.addToBalance(currency, 1000);
            acc.addToBalance(currency, 0);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Тест на положительное количество валюты провален!");
        }

        Assertions.assertEquals("TestMan", acc.getOwnerName());
        Assertions.assertEquals(0, acc.getBalanceByCurrency(currency));
        System.out.println("OK");
    }

    @Test
    @DisplayName("Test 5: Пользовательское исключение при отмене с пустым стеком команд")
    public void canUndoNothing(){
        Account acc = new Account("Bob");
        Assertions.assertThrows(NothingToUndo.class, ()->
                acc.undo());
        System.out.println("OK");
    }

    @Test
    @DisplayName("Test 6: Проверка отмены изменения суммы в балансе")
    public void checkUndoAmount(){
        Account acc = new Account("Bob");
        try {
            acc.addToBalance(ECurrency.USD, 100);
            acc.addToBalance(ECurrency.USD, 500);
            acc.showBalance();
            acc.showCommands();
            acc.undo();
        } catch (IllegalArgumentException e) {}

        Assertions.assertEquals(100, acc.getBalanceByCurrency(ECurrency.USD));

        System.out.println("OK");
    }

    @Test
    @DisplayName("Test 7: Проверка отмены изменения владельца счета")
    public void checkUndoOwnerName(){
        Account acc = new Account("Bob");
        try {
            acc.setOwnerName("Alex");
            acc.undo();
        } catch (IllegalArgumentException e) {}

        Assertions.assertEquals("Bob", acc.getOwnerName());

        System.out.println("OK");
    }

    @Test
    @DisplayName("Test 8: Проверка возможности отмены действия")
    public void checkIsUndoPossible(){
        Account acc = new Account("TestMan");
        acc.addToBalance(ECurrency.USD, 500);

        assert acc.canUndo() : "Тест на проверку возможности отмены действия провален (отмена возможна)!";
        acc.undo();
        assert !acc.canUndo() : "Тест на проверку возможности отмены действия провален (отмена невозможна)!";
    }

    private boolean balancesAreEqual(Map<ECurrency, Integer> benchmark, Map<ECurrency, Integer> asset) {
        if (benchmark.size() != asset.size()) {
            return false;
        }

        return benchmark.entrySet().stream()
                .allMatch(e -> e.getValue().equals(asset.get(e.getKey())));
    }
    @Test
    @DisplayName("Test 9: Проверка возможности загрузки состояния объекта")
    public void checkLoadState()
    {
        Account acc = new Account("Owner");
        Map<ECurrency, Integer> etalonMap = new HashMap<>();
        etalonMap.put(ECurrency.RUB, 1000);
        etalonMap.put(ECurrency.USD, 500);

        for(Map.Entry<ECurrency, Integer> item : etalonMap.entrySet()){
            acc.addToBalance(item.getKey(), item.getValue());
        }

        Loadable accSaved = acc.save();

        acc.setOwnerName("NewOwner");
        acc.addToBalance(ECurrency.USD, 4400);
        acc.load(accSaved);

        Assertions.assertEquals("Owner", acc.getOwnerName());
        Assertions.assertTrue(balancesAreEqual(etalonMap, acc.getBalance()));

        System.out.println("OK");
    }




}
