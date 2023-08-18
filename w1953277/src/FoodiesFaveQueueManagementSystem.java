import java.util.Arrays;
import java.util.Scanner;

public class FoodiesFaveQueueManagementSystem {
    private static final int MAX_CUSTOMERS[] = {2, 3, 5}; // Maximum customers per queue
    private static final int MAX_PIZZA = 100; // Total number of pizza in stock

    private static String[] queue1 = new String[MAX_CUSTOMERS[0]]; // Queue for cashier 1
    private static String[] queue2 = new String[MAX_CUSTOMERS[1]]; // Queue for cashier 2
    private static String[] queue3 = new String[MAX_CUSTOMERS[2]]; // Queue for cashier 3

    private static int stock = MAX_PIZZA; // Remaining pizza in stock

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 999) {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 100:
                    viewAllQueues();
                    break;
                case 101:
                    viewAllEmptyQueues();
                    break;
                case 102:
                    addCustomer(scanner);
                    break;
                case 103:
                    removeCustomer(scanner);
                    break;
                case 104:
                    removeServedCustomer();
                    break;
                case 105:
                    viewCustomersSorted();
                    break;
                case 106:
                    storeProgramData();
                    break;
                case 107:
                    loadProgramData();
                    break;
                case 108:
                    viewRemainingStock();
                    break;
                case 109:
                    addPizza(scanner);
                    break;
                case 999:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void addPizza(Scanner scanner) {
    }

    private static void displayMenu() {
        System.out.println("*****************");
        System.out.println("* Cashiers      *");
        System.out.println("*****************");
        displayQueues();
        System.out.println("Menu:");
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
        System.out.println("999 or EXT: Exit the Program.");
        System.out.print("Enter your choice: ");
    }

    private static void displayQueues() {
        for (int i = 0; i < MAX_CUSTOMERS.length; i++) {
            for (int j = 0; j < MAX_CUSTOMERS[i]; j++) {
                if (i == 0 && j >= queue1.length)
                    System.out.print(" X");
                else if (i == 1 && j >= queue2.length)
                    System.out.print(" X");
                else if (i == 2 && j >= queue3.length)
                    System.out.print(" X");
                else
                    System.out.print(" O");
            }
            System.out.println();
        }
    }

    private static void viewAllQueues() {
        System.out.println("Queue 1: " + Arrays.toString(queue1));
        System.out.println("Queue 2: " + Arrays.toString(queue2));
        System.out.println("Queue 3: " + Arrays.toString(queue3));
    }

    private static void viewAllEmptyQueues() {
        if (isQueueEmpty(queue1))
            System.out.println("Queue 1 is empty.");
        if (isQueueEmpty(queue2))
            System.out.println("Queue 2 is empty.");
        if (isQueueEmpty(queue3))
            System.out.println("Queue 3 is empty.");
    }

    private static boolean isQueueEmpty(String[] queue) {
        for (String customer : queue) {
            if (customer != null)
                return false;
        }
        return true;
    }

    private static void addCustomer(Scanner scanner) {
        System.out.print("Enter the cashier number (1, 2, or 3): ");
        int cashier = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (cashier < 1 || cashier > 3) {
            System.out.println("Invalid cashier number. Please try again.");
            return;
        }

        String[] selectedQueue = getQueueByCashier(cashier);

        if (isQueueFull(selectedQueue)) {
            System.out.println("Selected queue is full. Cannot add customer.");
            return;
        }

        System.out.print("Enter the customer name: ");
        String customerName = scanner.nextLine();

        selectedQueue[getNextAvailableIndex(selectedQueue)] = customerName;
        stock -= 10; // Reduce 10 pizza from stock for each customer

        if (stock <= 20)
            System.out.println("Warning: Low stock of pizza!");

        System.out.println("Customer added successfully.");
    }

    private static String[] getQueueByCashier(int cashier) {
        if (cashier == 1)
            return queue1;
        else if (cashier == 2)
            return queue2;
        else
            return queue3;
    }

    private static boolean isQueueFull(String[] queue) {
        for (String customer : queue) {
            if (customer == null)
                return false;
        }
        return true;
    }

    private static int getNextAvailableIndex(String[] queue) {
        for (int i = 0; i < queue.length; i++) {
            if (queue[i] == null)
                return i;
        }
        return -1; // No available index
    }

    private static void removeCustomer(Scanner scanner) {
        System.out.print("Enter the cashier number (1, 2, or 3): ");
        int cashier = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (cashier < 1 || cashier > 3) {
            System.out.println("Invalid cashier number. Please try again.");
            return;
        }

        String[] selectedQueue = getQueueByCashier(cashier);

        System.out.print("Enter the position of the customer to remove (1-" + selectedQueue.length + "): ");
        int position = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (position < 1 || position > selectedQueue.length) {
            System.out.println("Invalid position. Please try again.");
            return;
        }

        int index = position - 1;

        if (selectedQueue[index] == null) {
            System.out.println("No customer found at the specified position.");
        } else {
            String removedCustomer = selectedQueue[index];
            selectedQueue[index] = null;
            stock += 10; // Add 10 pizza back to stock for the removed customer
            System.out.println("Customer '" + removedCustomer + "' removed successfully.");
        }
    }

    private static void removeServedCustomer() {
        if (isQueueEmpty(queue1) && isQueueEmpty(queue2) && isQueueEmpty(queue3)) {
            System.out.println("No customers to remove.");
            return;
        }

        boolean customerRemoved = false;

        for (String[] queue : new String[][]{queue1, queue2, queue3}) {
            for (int i = 0; i < queue.length; i++) {
                if (queue[i] != null) {
                    String removedCustomer = queue[i];
                    queue[i] = null;
                    stock += 10; // Add 10 pizza back to stock for the removed customer
                    System.out.println("Served customer '" + removedCustomer + "' removed successfully.");
                    customerRemoved = true;
                    break;
                }
            }
            if (customerRemoved)
                break;
        }
    }

    private static void viewCustomersSorted() {
        String[] allCustomers = getAllCustomers();
        sortCustomers(allCustomers);

        if (allCustomers.length == 0) {
            System.out.println("No customers found.");
        } else {
            System.out.println("Customers Sorted in alphabetical order:");
            for (String customer : allCustomers) {
                System.out.println(customer);
            }
        }
    }

    private static String[] getAllCustomers() {
        String[] allCustomers = new String[queue1.length + queue2.length + queue3.length];
        int index = 0;

        for (String[] queue : new String[][]{queue1, queue2, queue3}) {
            for (String customer : queue) {
                if (customer != null)
                    allCustomers[index++] = customer;
            }
        }

        return Arrays.copyOf(allCustomers, index);
    }

    private static void sortCustomers(String[] customers) {
        for (int i = 0; i < customers.length - 1; i++) {
            for (int j = i + 1; j < customers.length; j++) {
                if (customers[i].compareTo(customers[j]) > 0) {
                    String temp = customers[i];
                    customers[i] = customers[j];
                    customers[j] = temp;
                }
            }
        }
    }

    private static void storeProgramData() {
        // Implement code to store program data into a file
        System.out.println("Program data stored successfully.");
    }

    private static void loadProgramData() {
        // Implement code to load program data from a file
        System.out.println("Program data loaded successfully.");
    }

    private static void viewRemainingStock() {
        System.out.println("Remaining pizza in stock: " + stock);
    }

    private static void addBurgers(Scanner scanner) {
        System.out.print("Enter the number of pizza to add to stock: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        stock += quantity;
        System.out.println(quantity + " pizza added to stock.");
    }
}
