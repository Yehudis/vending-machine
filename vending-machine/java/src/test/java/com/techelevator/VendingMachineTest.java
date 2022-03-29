package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class VendingMachineTest {
    private VendingMachine vm;

    @Rule
    public ExpectedException ee = ExpectedException.none();

    @Before
    public void set_up_vending_machine() throws Exception {
        try{
            vm = new VendingMachine();
            vm.feedMoney("20");
            for(int i = 0; i < 5; i++){
                vm.selectProduct("a1");
            }
        } catch (Exception e){
           throw new Exception(e.getMessage());
        }
    }

    @Test
    public void test_feed_money_negative_exception() throws Exception {
        ee.expectMessage("Please enter a whole positive number");
        vm.feedMoney("-1");
    }
    @Test
    public void test_feed_money_string_exception() throws Exception {
        ee.expectMessage("Please enter a whole positive number");
        vm.feedMoney("Yides");
    }
    @Test
    public void test_feed_money_double_exception() throws Exception {
        ee.expectMessage("Please enter a whole positive number");
        vm.feedMoney("5.5");
    }
    @Test
    public void test_feed_money() throws Exception {
        Assert.assertEquals(new BigDecimal("4.75"), vm.getBalance());
        vm.feedMoney("10");
        Assert.assertEquals(new BigDecimal("14.75"), vm.getBalance());
        vm.feedMoney("5");
        Assert.assertEquals(new BigDecimal("19.75"), vm.getBalance());
    }

    @Test
    public void test_select_product_sold_out_exception() throws Exception {
        ee.expectMessage("Sorry, this item is sold out");
        vm.selectProduct("a1");
    }
    @Test
    public void test_select_product_insufficient_funds_exception() throws Exception {
        ee.expectMessage("Insufficient Funds");
        vm.selectProduct("a4");
        vm.selectProduct("a4");
    }
    @Test
    public void test_select_product_invalid_and_valid_selection() throws Exception {
        vm.feedMoney("20");
        Assert.assertEquals("Invalid Selection", vm.selectProduct("a5"));
        Assert.assertEquals("Glug Glug, Yum!", vm.selectProduct("c2"));
        Assert.assertEquals("Crunch Crunch, Yum!", vm.selectProduct("a4"));
        Assert.assertEquals("Munch Munch, Yum!", vm.selectProduct("b3"));
        Assert.assertEquals("Chew Chew, Yum!", vm.selectProduct("d1"));
    }

    @Test
    public void test_get_change() throws Exception {
        Assert.assertEquals("Quarters: 19\nDimes: 0\nNickels: 0", vm.getChange());
    }

}
