package com.techelevator;

import java.math.BigDecimal;

import static java.lang.Math.abs;

public class Inventory {
    public String slotId;
    public String name;
    public BigDecimal price;
    public String type;
    public int inventoryCount;

    public Inventory(String slotId, String name, String price, String type) {
        this.slotId = slotId;
        this.name = name;
        this.price = new BigDecimal(price);
        this.type = type;
        inventoryCount = 5;
    }

    public String getSlotId() {
        return slotId;
    }
    public String getName() {
        return name;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public String getType() {
        return type;
    }
    public int getInventoryCount() {
        return inventoryCount;
    }

    @Override
    public String toString(){
        if (inventoryCount < 1){
            return "Slot ID: " + slotId + " Name: " + name + " SOLD OUT";
        }
        return "Slot ID: " + slotId + " Name: " + name + " Price: " + price;
    }
    public void reduceInventory(){
        inventoryCount--;
    }
    public int getInventorySold() {
        return abs(inventoryCount - 5);
    }
    public String getNoise(){
        String noise = "";
        if (type.equals("Drink")){
            noise = "Glug Glug, Yum!";
        }else if (type.equals("Candy")){
            noise = "Munch Munch, Yum!";
        }else if (type.equals("Chip")){
            noise = "Crunch Crunch, Yum!";
        }else if (type.equals("Gum")){
            noise = "Chew Chew, Yum!";
        }
        return noise;
    }
}
