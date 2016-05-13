package myaccount.mydetails;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;

public class CustomerBean extends Base {

    private String customerNumber;
    private String title;
    private String firstName;
    private String lastName;
    private String email;

    public CustomerBean() {
    }

    public CustomerBean(final Customer customer) {
        this.customerNumber = customer.getCustomerNumber();
        this.title = customer.getTitle();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(final String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
