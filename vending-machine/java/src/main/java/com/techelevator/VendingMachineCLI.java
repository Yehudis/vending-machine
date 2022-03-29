package com.techelevator;

import com.techelevator.view.Menu;

import java.math.BigDecimal;
import java.util.Scanner;

public class VendingMachineCLI {
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_REPORT = "Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_REPORT};

	private Menu menu;
	private VendingMachine vm;
	private Scanner scanner;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() throws Exception {
		try {
			vm = new VendingMachine();
			scanner = new Scanner(System.in);
		} catch (Exception e){
			throw new Exception(e.getMessage());
		}

		boolean exitProgram = false;
		while (!exitProgram) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				handleDisplay();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				handlePurchaseMenu();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				// finish transaction
				exitProgram = true;
			} else if (choice.equals(MAIN_MENU_OPTION_REPORT)) {
				// print report
				handlePrintReport();
			}
		}
	}

	private void handlePrintReport() {
		try{
			vm.writeReport();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private void handlePurchaseMenu() throws Exception {
		boolean notFinishedTransaction = true;
		while(notFinishedTransaction){
			System.out.println();
			System.out.println("\tPlease choose an option:");
			System.out.println("\t1) Feed Money");
			System.out.println("\t2) Select Product");
			System.out.println("\t3) Finish Transaction");
			System.out.println();

			String userInput = scanner.nextLine();
			if (userInput.equals("1")) {
				//feed money
				handleFeedMoney();
			} else if (userInput.equals("2")) {
				if(vm.getBalance().compareTo(BigDecimal.ZERO) == 1){
					handleItemSelection();
				} else {
					System.out.println();
					System.out.println("Please make a deposit before making a purchase");
					System.out.println();
				}

			} else if (userInput.equals("3")) {
				notFinishedTransaction = false;
				System.out.println(vm.getChange());
			}
		}
	}

	private void handleItemSelection() {
		System.out.println();
		System.out.println("Select an item: ");
		String itemSelection = scanner.nextLine();
		System.out.println();
		try {
			System.out.println(vm.selectProduct(itemSelection));
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		System.out.println();
		System.out.println("Your total balance is: " + vm.getBalance());
		System.out.println();
	}

	private void handleFeedMoney() {
		System.out.println();
		System.out.println("How much do you want to deposit?");
		String deposit = scanner.nextLine();
		try{
			vm.feedMoney(deposit);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		System.out.println();
		System.out.println("Your total balance is: " + vm.getBalance());
		System.out.println();
	}

	private void handleDisplay() {
		System.out.println();
		for(Inventory item : vm.getInventory()){
			System.out.println(item);
		}
	}

	public static void main(String[] args) throws Exception{
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

}
