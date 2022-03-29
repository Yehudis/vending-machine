package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
    private List<Inventory> inventory;
    private BigDecimal balance;
    private BigDecimal totalMoneyFed;
    private boolean notFirstAudit;
    private boolean firstEntrySalesReport;
    private LocalDateTime dateTime;

    public VendingMachine() throws Exception {
        try {
            inventory = setInventoryList();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
        balance = BigDecimal.ZERO;
        totalMoneyFed = BigDecimal.ZERO;
        dateTime = LocalDateTime.now();
        notFirstAudit = false;
        firstEntrySalesReport = false;
    }

    private List<Inventory> setInventoryList() throws Exception {
        List<Inventory> starterInventory = new ArrayList<>();

        File file = new File("Documentation/VendingMachine.txt");
        try (Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()){
                String[] item = scanner.nextLine().split("\\|");
                starterInventory.add(new Inventory(item[0], item[1], item[2], item[3]));
            }
        } catch (FileNotFoundException e) {
            throw new Exception("File is not found");
        }

        return starterInventory;
    }
    public List<Inventory> getInventory() {
        return inventory;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    private String getDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyy HH:mm:ss a");
        return dateTime.format(formatter);
    }

    public void feedMoney(String deposit) throws Exception {
        try {
            int testWholeNumber = Integer.parseInt(deposit);
            if (new BigDecimal(deposit).compareTo(BigDecimal.ONE) == -1){
                throw new Exception("Please enter a whole positive number");
            }

            balance = balance.add(new BigDecimal(deposit));
            totalMoneyFed = totalMoneyFed.add(new BigDecimal(deposit));
            writeAudit(getDateTime() + " FEED MONEY: " + deposit + " " + balance);
        } catch (NumberFormatException e){
            throw new Exception("Please enter a whole positive number");
        }
    }
    public String selectProduct(String item) throws Exception {
        try {
            for(int i = 0; i < inventory.size(); i++){
                String slotOption = inventory.get(i).getSlotId();
                if (item.equalsIgnoreCase(slotOption)){
                    if (inventory.get(i).getInventoryCount() < 1){
                        throw new Exception("Sorry, this item is sold out");
                    }
                    if (balance.compareTo(inventory.get(i).getPrice()) == -1){
                        throw new Exception("Insufficient Funds");
                    }

                    // reduce balance and inventory
                    inventory.get(i).reduceInventory();
                    balance = balance.subtract(inventory.get(i).getPrice());

                    writeAudit(getDateTime() + " " + inventory.get(i).getName() + " " + inventory.get(i).getSlotId() + " " + inventory.get(i).getPrice() + " " + balance);

                    return inventory.get(i).getNoise();
                }
            }
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return "Invalid Selection";
    }
    public String getChange() throws Exception {
        BigDecimal oldBalance = balance;
        totalMoneyFed = totalMoneyFed.subtract(balance);

        try{
            writeAudit(getDateTime() + " GIVE CHANGE: " + oldBalance + " " + balance);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

        return "Quarters: " + changeCalculation(".25") + "\nDimes: " + changeCalculation(".10") + "\nNickels: " + changeCalculation(".05");
    }
    private BigDecimal changeCalculation(String cents){
        BigDecimal quarters = balance.divide(new BigDecimal(cents));
        balance = balance.remainder(new BigDecimal(cents));
        quarters = quarters.subtract(balance.divide(new BigDecimal(cents)));
        return quarters;
    }
    private void writeAudit(String toWrite) throws Exception {
        try(PrintWriter pw = new PrintWriter(new FileOutputStream("Documentation/audit.txt", notFirstAudit))){
            notFirstAudit = true;
            pw.println(toWrite);
        } catch (FileNotFoundException e) {
            throw new Exception("File is not found");
        }
    }
    public void writeReport() throws Exception {
        try(PrintWriter pw = new PrintWriter(new FileOutputStream("Documentation/report.txt", firstEntrySalesReport))){
            firstEntrySalesReport = true;
            for (int i = 0; i < inventory.size(); i++){
                pw.println(inventory.get(i).getName() + " | " + inventory.get(i).getInventorySold());
            }
            pw.println();
            pw.println("**TOTAL SALES** " + totalMoneyFed);
        } catch (FileNotFoundException e) {
            throw new Exception("File is not found");
        }
    }
}
