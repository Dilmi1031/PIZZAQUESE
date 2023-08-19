import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final int MAX_PIZZA = 100;
    private static final int[] MAX_CUSTOMERS = { 2, 3, 5 };
    private static final int WARNING_THRESHOLD = 20;
    private static final int PIZZA_PRICE = 1350;
    private static FoodQueue[] queues;

    public static void main(String[] args) throws InterruptedException, IOException {
        initializeQueues();
        Scanner scanner = new Scanner(System.in);
        boolean exitProgram = false;

        while (!exitProgram) {
            displayMenu();
            String choice = scanner.nextLine();

            if (choice.equals("100") || choice.equals("VFQ")) {
                viewAllQueues(false);
            } else if (choice.equals("101") || choice.equals("VEQ")) {
                viewAllQueues(true);
            } else if (choice.equals("102") || choice.equals("ACQ")) {
                addCustomerToQueue(scanner);
            } else if (choice.equals("103") || choice.equals("RCQ")) {
                removeCustomerFromQueue(scanner);
            } else if (choice.equals("104") || choice.equals("PCQ")) {
                removeServedCustomer();
            } else if (choice.equals("105") || choice.equals("VCS")) {
                viewCustomersSortedAlphabetically();
            } else if (choice.equals("106") || choice.equals("SPD")) {
                storeProgramDataToFile();
            } else if (choice.equals("107") || choice.equals("LPD")) {
                loadProgramDataFromFile();
            } else if (choice.equals("108") || choice.equals("STK")) {
                viewRemainingPizzaStock();
            } else if (choice.equals("109") || choice.equals("AFS")) {
                addPizzaToStock(scanner);
            } else if (choice.equals("110") || choice.equals("IFQ")) {
                viewIncomeOfEachQueue();
            } else if (choice.equals("999") || choice.equals("EXT")) {
                System.out.println("Exiting the program. Goodbye!");
                exitProgram = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void clearConsole() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    private static void initializeQueues() {
        queues = new FoodQueue[MAX_CUSTOMERS.length];
        for (int i = 0; i < MAX_CUSTOMERS.length; i++) {
            queues[i] = new FoodQueue(MAX_CUSTOMERS[i]);
        }
    }

    private static void displayMenu() {
        System.out.println("Menu Options:");
        System.out.println("100 or VFQ: View all Queues.");
        System.out.println("101 or VEQ: View all Empty Queues.");
        System.out.println("102 or ACQ: Add customer to a Queue.");
        System.out.println("103 or RCQ: Remove a customer from a Queue.");
        System.out.println("104 or PCQ: Remove a served customer.");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order.");
        System.out.println("106 or SPD: Store Program Data into file.");
        System.out.println("107 or LPD: Load Program Data from file.");
        System.out.println("108 or STK: View Remaining pizza Stock.");
        System.out.println("109 or AFS: Add pizza to Stock.");
        System.out.println("110 or IFQ: View Income of each Queue.");
        System.out.println("999 or EXT: Exit the Program.");
        System.out.print("Enter your choice: ");
    }

    private static void viewAllQueues(boolean printEmptyQueues) {
        for (int i = 0; i < MAX_CUSTOMERS.length; i++) {
            System.out.print("Cashier " + (i + 1) + ": ");
            for (int j = 0; j < MAX_CUSTOMERS[i]; j++) {
                if (j < queues[i].getQueueLength()) {
                    if (queues[i].getCustomer(j) != null) {
                        if (printEmptyQueues) {
                            System.out.print("- ");
                        } else {
                            System.out.print("O ");
                        }
                    } else {
                        System.out.print("X ");
                    }
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    private static void addCustomerToQueue(Scanner scanner) throws InterruptedException, IOException {
        clearConsole();
        int shortestQueueIndex = getShortestQueueIndex();
        if (shortestQueueIndex == -1) {
            System.out.println("All queues are full. Adding customer to the waiting list.");
            System.out.print("Enter the customer's first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter the customer's last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter the customer's ID: ");
            int numID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter the number of pizza required: ");
            int numPizza = scanner.nextInt();
            scanner.nextLine();

            Customer newCustomer = new Customer(firstName, lastName, numID, numPizza);
            queues[0].addCustomerToWaitingList(newCustomer);
            updateStock(-numPizza);

            if (getRemainingPizza() <= WARNING_THRESHOLD) {
                System.out.println("Warning: Low stock of pizza!");
            }
            return;
        }

        System.out.print("Enter the customer's first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter the customer's last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter the customer's ID: ");
        int numID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the number of pizza required: ");
        int numPizza = scanner.nextInt();
        scanner.nextLine();

        Customer newCustomer = new Customer(firstName, lastName, numID, numPizza);
        queues[shortestQueueIndex].addCustomer(newCustomer);
        updateStock(-numPizza);

        if (getRemainingPizza() <= WARNING_THRESHOLD) {
            System.out.println("Warning: Low stock of pizza!");
        }
    }

    private static void removeCustomerFromQueue(Scanner scanner) throws InterruptedException, IOException {
        System.out.print("Enter the queue number (1 to " + MAX_CUSTOMERS.length + "): ");
        int queueNumber = scanner.nextInt() - 1;
        scanner.nextLine();

        if (queueNumber < 0 || queueNumber >= MAX_CUSTOMERS.length) {
            System.out.println("Invalid queue number.");
            return;
        }

        if (queues[queueNumber].getQueueLength() == 0) {
            System.out.println("The queue is already empty.");
            return;
        }

        System.out.print("Enter the index of the customer to remove (1 to " + queues[queueNumber].getQueueLength() + "): ");
        int customerIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (customerIndex < 0 || customerIndex >= queues[queueNumber].getQueueLength()) {
            System.out.println("Invalid customer index.");
            return;
        }

        Customer removedCustomer = queues[queueNumber].removeCustomer(customerIndex);
        updateStock(removedCustomer.getNumPizza());
        System.out.println("Removed customer: " + removedCustomer.getFullName());
    }

    private static void removeServedCustomer() throws InterruptedException, IOException {
        clearConsole();
        for (FoodQueue queue : queues) {
            if (queue.getQueueLength() > 0) {
                Customer removedCustomer = queue.removeCustomer(0);
                updateStock(removedCustomer.getNumPizza());
                System.out.println("Removed served customer: " + removedCustomer.getFullName());

                if (queues[0].getWaitingListSize() > 0) {
                    Customer nextCustomer = queues[0].removeCustomerFromWaitingList();
                    queues[0].addCustomer(nextCustomer);
                    updateStock(-nextCustomer.getNumPizza());
                    System.out.println("Moved customer from waiting list to food queue: " + nextCustomer.getFullName());
                }
                return;
            }
        }

        System.out.println("No served customers to remove.");
    }

    private static void viewCustomersSortedAlphabetically() throws InterruptedException, IOException {
        clearConsole();
        List<Customer> allCustomers = new ArrayList<>();
        List<String> customersName = new ArrayList<>();
        for (FoodQueue queue : queues) {
            allCustomers.addAll(queue.getCustomers());
        }

        if (allCustomers.isEmpty()) {
            System.out.println("No customers in the queues.");
            return;
        }
        System.out.println(allCustomers.get(0).getFullName());


        allCustomers.forEach(customer->customersName.add(customer.getFullName()));

        Collections.sort(customersName);


        System.out.println("Customers Sorted in alphabetical order:");
        for (String customer : customersName) {
            System.out.println(customer);
        }
    }

    private static void storeProgramDataToFile() throws InterruptedException, IOException {
        clearConsole();

        try (PrintWriter myWriter = new PrintWriter("data.txt")) {
            for (int i = 0; i < MAX_CUSTOMERS.length; i++) {
                myWriter.print("Cashier " + (i + 1) + ": ");
                for (int j = 0; j < MAX_CUSTOMERS[i]; j++) {
                    if (queues[i].getCustomer(j) != null) {
                        myWriter.print("O ");
                    } else {
                        myWriter.print("X ");
                    }
                }
                myWriter.println();
            }
            System.out.println("Program data stored successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void loadProgramDataFromFile() throws InterruptedException, IOException {
        clearConsole();

        try (FileReader reader = new FileReader("data.txt")) {
            int data = reader.read();
            while (data != -1) {
                data = reader.read();
            }
            System.out.println("Program data loaded successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void viewRemainingPizzaStock() throws InterruptedException, IOException {
        clearConsole();
        System.out.println("Remaining pizza stock: " + getRemainingPizza());
    }

    private static void addPizzaToStock(Scanner scanner) throws InterruptedException, IOException {
        System.out.print("Enter the number of pizza to add: ");
        int numPizza = scanner.nextInt();
        scanner.nextLine();

        if (numPizza <= 0) {
            System.out.println("Invalid number of pizza.");
            return;
        }

        updateStock(numPizza);
        System.out.println("Added " + numPizza + " pizza to the stock.");
    }

    private static void viewIncomeOfEachQueue() throws InterruptedException, IOException {
        clearConsole();
        System.out.println("Price Of a Pizza: $" + PIZZA_PRICE);
        for (int i = 0; i < MAX_CUSTOMERS.length; i++) {
            int queueIncome = queues[i].getTotalPizza() * PIZZA_PRICE;
            System.out.println("Income of Queue " + (i + 1) + ": $" + queueIncome);
        }
    }

    private static int getRemainingPizza() {
        return MAX_PIZZA - getStock();
    }

    private static int getStock() {
        int stock = 0;
        for (FoodQueue queue : queues) {
            stock += queue.getTotalPizza();
        }
        return stock;
    }

    private static void updateStock(int pizza) throws InterruptedException, IOException {
        clearConsole();

        int currentStock = getStock();
        currentStock += pizza;

        if (currentStock < 0) {
            currentStock = 0;
        } else if (currentStock > MAX_PIZZA) {
            currentStock = MAX_PIZZA;
        }

        int remainingPizza = MAX_PIZZA - currentStock;
        System.out.println("Remaining pizza stock: " + remainingPizza);

        if (remainingPizza <= WARNING_THRESHOLD) {
            System.out.println("Warning: Low stock of pizza!");
        }
    }

    private static int getShortestQueueIndex() {
        int shortestQueueIndex = -1;
        int shortestQueueLength = Integer.MAX_VALUE;

        for (int i = 0; i < queues.length; i++) {
            int queueLength = queues[i].getQueueLength();
            if (queueLength < MAX_CUSTOMERS[i] && queueLength < shortestQueueLength) {
                shortestQueueIndex = i;
                shortestQueueLength = queueLength;
            }
        }

        return shortestQueueIndex;
    }
}
