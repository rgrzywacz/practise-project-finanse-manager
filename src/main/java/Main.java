import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

import account.AccountRepository;
import account.AccountDto;
import account.AccountService;
import category.CategoryRepository;
import category.CategoryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ConnectionManager;
import eception.ApplicationException;
import expanse.ExpenseRepository;
import expanse.ExpenseDto;
import expanse.ExpenseService;
import expanse.PrintExpenseDto;
import income.*;
import summary.SummaryDto;
import summary.SummaryExtendDtos;
import summary.SummaryService;
import validation.ValidationMessage;

public class Main {
    private static final AccountRepository ACCOUNT_REPOSITORY = new AccountRepository();
    private static final AccountService accountService = new AccountService(ACCOUNT_REPOSITORY);
    private static final CategoryRepository CATEGORY_REPOSITORY = new CategoryRepository();
    private static final CategoryService categoryService = new CategoryService(CATEGORY_REPOSITORY);
    private static final ExpenseRepository EXPENSE_REPOSITORY = new ExpenseRepository();
    private static final ExpenseService expenseService = new ExpenseService(EXPENSE_REPOSITORY, CATEGORY_REPOSITORY, ACCOUNT_REPOSITORY);
    private static final IncomeRepository INCOME_REPOSITORY = new IncomeRepository();
    private static final IncomeService incomeService = new IncomeService(INCOME_REPOSITORY, ACCOUNT_REPOSITORY);
    private static final SummaryService summaryService = new SummaryService(EXPENSE_REPOSITORY, INCOME_REPOSITORY);
    private static final Scanner in = new Scanner(System.in).useLocale(Locale.ROOT);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        ConnectionManager.getEntityManager();
        List<String> categories = categoryService.getAllCategoryNames();

        while (true) {
            System.out.println("Type the operation to execution: ");

            System.out.println("1 - Add new account");
            System.out.println("2 - Display all accounts");
            System.out.println("3 - Delete account");
            System.out.println("4 - Add expense");
            System.out.println("5 - Add income");
            System.out.println("6 - Delete expense");
            System.out.println("7 - Delete income");
            System.out.println("8 - Display all expenses and incomes"); // Wyświetl wszystkie wydatki i przychody
            System.out.println("9 - Display all expenses");
            System.out.println("10 - Display all incomes");
            System.out.println("11 - Display balance");
            System.out.println("12 - Display all expenses grouping by category");
            System.out.println("13 - Display all expenses between dates");
            System.out.println("14 - Add new category");
            System.out.println("15 - Delete category");

            int result = in.nextInt();
            List<AccountDto> accounts = accountService.getAccounts();
            switch (result) {
                case 1 -> {
                    System.out.println("Type account number:");
                    in.nextLine();
                    String accountNumber = in.nextLine();
                    String accountNumberWithoutSpaces = accountNumber.replaceAll("\\s","");
                    System.out.println("Type account name:");
                    String accountName = in.next();
                    accountService.addAccount(accountNumberWithoutSpaces, accountName);
                    accounts = accountService.getAccounts();
                }
                case 2 -> System.out.println(gson.toJson(accounts));
                case 3 -> {
                    System.out.println("Select the account you want to delete: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        try {
                            accountService.deleteAccount(accountId);
                            accounts = accountService.getAccounts();
                        } catch (IllegalArgumentException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Select the account to which you relate the expense: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    String accountNumber = first.get().getAccountNumber();
                    System.out.println("Type expense amount: ");
                    BigDecimal totalCost = new BigDecimal(String.valueOf(in.nextBigDecimal())).setScale(2, RoundingMode.CEILING);
                    in.nextLine();
                    System.out.println("Type expense category: " + categories.toString());
                    String category = in.nextLine();
                    System.out.println("Type comment (optionally): ");
                    String comment = in.nextLine();
                    ExpenseDto expenseDto = new ExpenseDto(totalCost, comment, category,accountNumber);
                    try {
                        expenseService.addExpense(expenseDto);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
                case 5 -> {
                    System.out.println("Select the account to which you relate the income: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    String accountNumber = first.get().getAccountNumber();

                    System.out.println("Type income amount: ");
                    BigDecimal totalCost = new BigDecimal(String.valueOf(in.nextBigDecimal()));
                    System.out.println("Type comment (optionally): ");
                    String comment = in.next();

                    IncomeDto incomeDto = new IncomeDto(totalCost, comment, accountNumber);
                    try {
                        incomeService.addIncome(incomeDto);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
                case 6 -> {
                    System.out.println("Type expense id which you want to delete: ");
                    Long expensedToBeDeleted = in.nextLong();
                    expenseService.deleteExpense(expensedToBeDeleted);
                }
                case 7 -> {
                    System.out.println("Type income id which you want to delete: ");
                    Long incomeIdToBeDeleted = in.nextLong();
                    incomeService.deleteIncome(incomeIdToBeDeleted);
                }
                case 8 -> {
                    System.out.println("Select the account for which you want to display data: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        SummaryDto summary = summaryService.getSummary(accountId);
                        System.out.println(gson.toJson(summary));
                    } else {
                        System.err.println(ValidationMessage.ACCOUNT_NOT_EXISTS);
                    }

                }
                case 9 -> {
                    System.out.println("Select the account for which you want to display data: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        Set<PrintExpenseDto> expenses = expenseService.getExpensesByAccountId(accountId);
                        System.out.println(gson.toJson(expenses));
                    } else {
                        System.err.println(ValidationMessage.ACCOUNT_NOT_EXISTS);
                    }
                }
                case 10 -> {
                    System.out.println("Select the account for which you want to display data: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        Set<PrintIncomeDto> incomes = incomeService.getIncomesByAccountId(accountId);
                        System.out.println(gson.toJson(incomes));
                    } else {
                        System.err.println(ValidationMessage.ACCOUNT_NOT_EXISTS);
                    }

                }
                case 11 -> {
                    System.out.println("Select the account for which you want to display balance: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();
                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        String balance = summaryService.getBalanceForAccount(accountId);
                        System.out.println(balance +" zł");
                    } else {
                        System.err.println(ValidationMessage.ACCOUNT_NOT_EXISTS);
                    }

                }
                case 12 -> {
                    System.out.println("Select the account for which you want to display data: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();

                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        SummaryExtendDtos summaryExtendDtos = summaryService.summaryExtendDtosForAccount(accountId);
                        System.out.println(gson.toJson(summaryExtendDtos));
                    } else {
                        System.err.println(ValidationMessage.ACCOUNT_NOT_EXISTS);
                    }
                }
                case 13 -> {
                    System.out.println("Select the account for which you want to display data: ");
                    printAccounts(accounts);
                    long selectedAccount = in.nextLong();
                    Optional<AccountDto> first = accounts.stream().filter(a -> a.getId().equals(selectedAccount)).findFirst();

                    if (first.isPresent()) {
                        long accountId= first.get().getId();
                        System.out.println("Type start and end date in format yyyy-mm-dd");
                        System.out.println("Start date: ");
                        String startDate = in.next();
                        System.out.println("End date: ");
                        String endDate = in.next();
                        Set<PrintExpenseDto> expensesBetweenDate
                                = expenseService.getExpensesBetweenDateForAccount(startDate, endDate, accountId);
                        System.out.println(gson.toJson(expensesBetweenDate));
                    } else {
                        System.err.println(ValidationMessage.ACCOUNT_NOT_EXISTS);
                    }
                }
                case 14 -> {
                    System.out.println("Type category name: ");
                    in.nextLine();
                    String categoryName = in.nextLine();
                    categoryService.addCategory(categoryName);
                    categories = categoryService.getAllCategoryNames();
                }
                case 15 -> {
                    System.out.println("Type category name: ");
                    in.nextLine();
                    String categoryName = in.nextLine();
                    try {
                        categoryService.deleteCategory(categoryName);
                        categories = categoryService.getAllCategoryNames();
                    } catch (ApplicationException applicationException) {
                        System.err.println(applicationException.getMessage());
                    }
                }
            }
        }
    }

    private static void printAccounts(List<AccountDto> accounts) {
        for (AccountDto account : accounts) {
            System.out.println(account.getId()+") "+ account.getName() + " -- " + account.getAccountNumber());
        }
    }

}
