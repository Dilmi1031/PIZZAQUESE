import java.util.ArrayList;
import java.util.List;

class FoodQueue {
    private Customer[] customers;
    private Customer[] waitingList;
    private int waitingListSize;
    private int front;
    private int rear;
    private int totalPizza;

    public FoodQueue(int maxCustomers, int maxWaitingListSize) {
        customers = new Customer[maxCustomers];
        waitingList = new Customer[maxWaitingListSize];
        waitingListSize = 0;
        front = 0;
        rear = -1;
    }

    public FoodQueue(int maxCustomers) {
        this(maxCustomers, 0);
    }

    public int getQueueLength() {
        int length = 0;
        for (Customer customer : customers) {
            if (customer != null) {
                length++;
            }
        }
        return length;
    }

    public Customer getCustomer(int index) {
        if (index >= 0 && index < customers.length) {
            return customers[index];
        }
        return null;
    }

    public void addCustomer(Customer customer) {
        for (int i = 0; i < customers.length; i++) {
            if (customers[i] == null) {
                customers[i] = customer;
                return;
            }
        }
    }

    public Customer removeCustomer(int index) {
        if (index >= 0 && index < customers.length) {
            Customer removedCustomer = customers[index];
            customers[index] = null;
            return removedCustomer;
        }
        return null;
    }

    public List<Customer> getCustomers() {
        List<Customer> customerList = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer != null) {
                customerList.add(customer);
            }
        }
        return customerList;
    }

    public int getTotalPizza() {
        int totalBurgers = 0;
        for (Customer customer : customers) {
            if (customer != null) {
                totalPizza += customer.getNumPizza();
            }
        }
        return totalPizza;
    }

    public int getWaitingListSize() {
        return waitingListSize;
    }

    public Customer getWaitingListCustomer(int index) {
        if (index >= 0 && index < waitingListSize) {
            int actualIndex = (front + index) % waitingList.length;
            return waitingList[actualIndex];
        }
        return null;
    }

    public void addCustomerToWaitingList(Customer customer) {
        if (waitingListSize < waitingList.length) {
            rear = (rear + 1) % waitingList.length;
            waitingList[rear] = customer;
            waitingListSize++;
        } else {
            System.out.println("Waiting list is full. Customer cannot be added.");
        }
    }

    public Customer removeCustomerFromWaitingList() {
        if (waitingListSize > 0) {
            Customer removedCustomer = waitingList[front];
            waitingList[front] = null;
            front = (front + 1) % waitingList.length;
            waitingListSize--;
            return removedCustomer;
        }
        return null;
    }
}
