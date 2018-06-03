package com.capgemini.assessment.web.vaadin;

import com.capgemini.assessment.service.AccountService;
import com.capgemini.assessment.service.CustomerService;
import com.capgemini.assessment.service.TransactionService;
import com.capgemini.assessment.service.exception.AccountNotFoundException;
import com.capgemini.assessment.service.exception.CustomerAlreadyExistException;
import com.capgemini.assessment.service.exception.CustomerNotFoundException;
import com.capgemini.assessment.service.exception.InsufficientBalanceException;
import com.capgemini.assessment.service.model.input.account.AddAccountInput;
import com.capgemini.assessment.service.model.input.customer.AddCustomerInput;
import com.capgemini.assessment.service.model.input.transaction.TransactionInput;
import com.capgemini.assessment.service.model.output.account.GetAccountOutput;
import com.capgemini.assessment.service.model.output.account.AccountTransactionOutput;
import com.capgemini.assessment.service.model.output.customer.GetCustomerOutput;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Silay.Ugurlu on 29/05/2018
 */
@SpringView(name = TransactionView.NAME)
public class TransactionView extends VerticalLayout implements View {
    public static final String NAME = "TransactionView";

    @Autowired
    CustomerService customerService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    VerticalLayout accountLayout;

    VerticalLayout customerLayout;

    VerticalLayout transactionLayout;

    Grid<GetAccountOutput> getAccountOutputGrid = null;
    Grid<AccountTransactionOutput> transactionGrid = null;


    private long selectedCustomerId;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        customerLayout = buildCustomer();
        this.addComponent(customerLayout);
    }


    private VerticalLayout buildCustomer() {
        VerticalLayout verticalLayout = new VerticalLayout();
        Grid<GetCustomerOutput> customerOutputGrid = new Grid<>();
        customerOutputGrid.setCaption("Customers");
        customerOutputGrid.setItems(customerService.getAllCustomer());
        customerOutputGrid.addColumn(getCustomerOutput -> getCustomerOutput.getId()).setCaption("Id");
        customerOutputGrid.addColumn(getCustomerOutput -> getCustomerOutput.getName()).setCaption("Name");
        customerOutputGrid.addColumn(getCustomerOutput -> getCustomerOutput.getSurname()).setCaption("Surname");
        customerOutputGrid.addColumn(getCustomerOutput -> getCustomerOutput.getIdentityNumber()).setCaption("Identity Number");
        verticalLayout.addComponent(customerOutputGrid);
        Button addCustomer = new Button("Add Customer");
        addCustomer.addClickListener(event -> {
            Window window = new Window("Add Customer");
            window.setWidth(300.0f, Unit.PIXELS);
            final FormLayout content = new FormLayout();
            content.addComponent(new Label("Name"));
            TextField name = new TextField();
            content.addComponent(name);
            content.addComponent(new Label("Surname"));
            TextField surname = new TextField();
            content.addComponent(surname);
            content.addComponent(new Label("Identity Number"));
            TextField identityNumber = new TextField();
            content.addComponent(identityNumber);
            Button add = new Button("add");
            content.addComponent(add);
            add.addClickListener(event1 -> {
                try {
                    customerService.addCustomer(AddCustomerInput.builder().name(name.getValue()).surname(surname.getValue()).identityNumber(identityNumber.getValue()).build());
                    window.close();
                    customerOutputGrid.setItems(customerService.getAllCustomer());
                    customerOutputGrid.markAsDirty();
                } catch (CustomerAlreadyExistException customerAlreadyExistException) {
                    Notification.show(customerAlreadyExistException.getErrors().get(0), Notification.Type.ERROR_MESSAGE);
                }
            });
            content.setMargin(true);
            window.setContent(content);
            UI.getCurrent().addWindow(window);
        });
        customerOutputGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        customerOutputGrid.addSelectionListener(selectionEvent -> {
            selectionEvent.getFirstSelectedItem().ifPresent(getCustomerOutput -> {
                accountLayout = buildAccount(getCustomerOutput);
                TransactionView.this.addComponent(accountLayout);
            });
            if (!selectionEvent.getFirstSelectedItem().isPresent()) {
                TransactionView.this.removeComponent(accountLayout);
                if (transactionLayout != null) {
                    TransactionView.this.removeComponent(transactionLayout);
                }

            }
        });
        verticalLayout.addComponent(addCustomer);
        return verticalLayout;
    }


    private VerticalLayout buildAccount(GetCustomerOutput getCustomerOutput) {
        if (accountLayout != null) {
            accountLayout.removeAllComponents();
            if(transactionLayout != null){
                transactionLayout.removeAllComponents();
            }
        } else {
            accountLayout = new VerticalLayout();
        }

        getAccountOutputGrid = new Grid<>();
        getAccountOutputGrid.setCaption("Accounts");
        getAccountOutputGrid.setItems(accountService.getCustomerAccounts(getCustomerOutput.getId()));
        getAccountOutputGrid.addColumn(getAccountOutput -> getAccountOutput.getId()).setCaption("Id");
        getAccountOutputGrid.addColumn(getAccountOutput -> getAccountOutput.getCustomerId()).setCaption("Customer Id");
        getAccountOutputGrid.addColumn(getAccountOutput -> getAccountOutput.getCurrency()).setCaption("Currency");
        getAccountOutputGrid.addColumn(getAccountOutput -> getAccountOutput.getBalance()).setCaption("Balance");
        accountLayout.addComponent(getAccountOutputGrid);
        Button addCustomer = new Button("Add Account");
        addCustomer.addClickListener(event -> {
            Window window = new Window("Add Account");
            window.setWidth(300.0f, Unit.PIXELS);
            final FormLayout content = new FormLayout();
            content.addComponent(new Label("Currency"));
            TextField currencyType = new TextField();
            content.addComponent(currencyType);

            content.addComponent(new Label("Amount"));
            TextField amount = new TextField();
            amount.addValueChangeListener(new ValueEnterListener());
            content.addComponent(amount);

            Button add = new Button("add");
            content.addComponent(add);
            add.addClickListener(event1 -> {
                try {
                    AddAccountInput addAccountInput = AddAccountInput.builder().amount(new Long(amount.getValue())).currency(currencyType.getValue()).customerId(getCustomerOutput.getId()).build();
                    accountService.addAccount(addAccountInput);
                    window.close();
                    getAccountOutputGrid.setItems(accountService.getCustomerAccounts(getCustomerOutput.getId()));
                    getAccountOutputGrid.markAsDirty();
                } catch (CustomerNotFoundException | AccountNotFoundException | InsufficientBalanceException e) {
                    Notification.show(e.getErrors().get(0), Notification.Type.ERROR_MESSAGE);
                }
            });
            content.setMargin(true);
            window.setContent(content);
            UI.getCurrent().addWindow(window);
        });
        accountLayout.addComponent(addCustomer);
        getAccountOutputGrid.addSelectionListener(selectionEvent -> {
            selectionEvent.getFirstSelectedItem().ifPresent(getAccountOutput -> {
                transactionLayout = buildTransaction(getAccountOutput);
                TransactionView.this.addComponent(transactionLayout);
            });
            if (!selectionEvent.getFirstSelectedItem().isPresent()) {
                TransactionView.this.removeComponent(transactionLayout);
            }

        });
        return accountLayout;
    }


    private VerticalLayout buildTransaction(GetAccountOutput getAccountOutput) {
        if (transactionLayout != null) {
            transactionLayout.removeAllComponents();
        } else {
            transactionLayout = new VerticalLayout();
        }

        transactionGrid = new Grid<>();
        transactionGrid.setCaption("Transactions");
        try {
            transactionGrid.setItems(accountService.getAccountTransactions(getAccountOutput.getId()).getAccountTransactionOutputs());
        } catch (AccountNotFoundException accountNotFoundException) {
            accountNotFoundException.printStackTrace();
            return null;
        }
        transactionGrid.addColumn(transactionOutput -> transactionOutput.getAmount()).setCaption("Amount");
        transactionGrid.addColumn(transactionOutput -> transactionOutput.getTransactionDate()).setCaption("Date");
        transactionLayout.addComponent(transactionGrid);
        Button addTransaction = new Button("Add Transaction");
        addTransaction.addClickListener(event -> {
            Window window = new Window("Add Transaction");
            window.setWidth(300.0f, Unit.PIXELS);
            final FormLayout content = new FormLayout();

            content.addComponent(new Label("Amount"));
            TextField amount = new TextField(new ValueEnterListener());
            amount.addValueChangeListener(new ValueEnterListener());
            content.addComponent(amount);

            Button add = new Button("add");
            content.addComponent(add);
            add.addClickListener(event1 -> {
                try {
                    TransactionInput transactionInput = TransactionInput.builder().amount(new Long(amount.getValue())).accountId(getAccountOutput.getId()).build();
                    transactionService.makeTransaction(transactionInput);
                    transactionGrid.setItems(accountService.getAccountTransactions(getAccountOutput.getId()).getAccountTransactionOutputs());
                    getAccountOutputGrid.setItems(accountService.getCustomerAccounts(getAccountOutput.getCustomerId()));
                    window.close();
                } catch (AccountNotFoundException | InsufficientBalanceException e) {
                    Notification.show(e.getErrors().get(0), Notification.Type.ERROR_MESSAGE);
                }
            });
            content.setMargin(true);
            window.setContent(content);
            UI.getCurrent().addWindow(window);
        });
        transactionLayout.addComponent(addTransaction);
        return transactionLayout;
    }

}

class ValueEnterListener implements HasValue.ValueChangeListener<String> {
    @Override
    public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
        try {
            Long.parseLong(valueChangeEvent.getValue());
        }catch (Exception e){
            valueChangeEvent.getSource().setValue("");
        }

    }
}
