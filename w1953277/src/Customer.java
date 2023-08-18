class Customer {
    private String firstName;
    private String lastName;
    private String numID;
    private int numPizza;

    public Customer(String firstName, String lastName, int numID, int numPizza) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numID = this.numID;
        this.numPizza = numPizza;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getNumPizza() {
        return numPizza;
    }
}
