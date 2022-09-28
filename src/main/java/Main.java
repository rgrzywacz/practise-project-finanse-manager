import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

import category.CategoryDao;
import category.CategoryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ConnectionManager;
import eception.ApplicationException;
import expanse.ExpenseDao;
import expanse.ExpenseDto;
import expanse.ExpenseService;
import expanse.PrintExpenseDto;
import income.*;
import summary.SummaryDto;
import summary.SummaryExtendDtos;
import summary.SummaryService;

public class Main {

    private static final CategoryDao categoryDao = new CategoryDao();
    private static final CategoryService categoryService = new CategoryService(categoryDao);
    private static final ExpenseDao expenseDao = new ExpenseDao();
    private static final ExpenseService expenseService = new ExpenseService(expenseDao, categoryDao);
    private static final IncomeDao incomeDao = new IncomeDao();
    private static final IncomeService incomeService = new IncomeService(incomeDao);
    private static final SummaryService summaryService = new SummaryService(expenseDao, incomeDao);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Scanner in = new Scanner(System.in).useLocale(Locale.ROOT);

    public static void main(String[] args) {
        ConnectionManager.getEntityManager();
        List<String> categories;
        while (true) {
            System.out.println("Type the operation to execution: ");

            System.out.println("1 - Add expense");
            System.out.println("2 - Add income");
            System.out.println("3 - Delete expense");
            System.out.println("4 - Delete income");
            System.out.println("5 - Display all expenses and incomes"); // Wyświetl wszystkie wydatki i przychody
            System.out.println("6 - Display all expenses");
            System.out.println("7 - Display all incomes");
            System.out.println("8 - Display balance");
            System.out.println("9 - Display all expenses grouping by category");
            System.out.println("10 - Display all expenses between dates");
            System.out.println("11 - Add new category");
            System.out.println("12 - Delete category");

            int result = in.nextInt();
            switch (result) {
                case 1 -> {
                    categories = categoryService.getAllCategoryNames();
                    System.out.println("Type expense amount: ");
                    BigDecimal totalCost = new BigDecimal(String.valueOf(in.nextBigDecimal())).setScale(2, RoundingMode.CEILING);
                    in.nextLine();
                    System.out.println("Type expense category: "+ categories.toString());
                    String category = in.nextLine();
                    System.out.println("Type comment (optionally): ");
                    String comment = in.nextLine();
                    ExpenseDto expenseDto = new ExpenseDto(totalCost, comment, category);
                    try {
                        expenseService.addExpense(expenseDto);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.println("Type income amount: ");
                    BigDecimal totalCost = new BigDecimal(String.valueOf(in.nextBigDecimal()));
                    System.out.println("Type comment (optionally): ");
                    String comment = in.next();
                    IncomeDto incomeDto = new IncomeDto(totalCost, comment);
                    try {
                        incomeService.addIncome(incomeDto);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.println("Type expense id which you want to delete: ");
                    Long expensedToBeDeleted = in.nextLong();
                    expenseService.deleteExpense(expensedToBeDeleted);
                }
                case 4 -> {
                    System.out.println("Type income id which you want to delete: ");
                    Long incomeIdToBeDeleted = in.nextLong();
                    incomeService.deleteIncome(incomeIdToBeDeleted);
                }
                case 5 -> {
                    SummaryDto summary = summaryService.getSummary();
                    System.out.println(gson.toJson(summary));
                }
                case 6 -> {
                    Set<PrintExpenseDto> expenses = expenseService.getExpenses();
                    System.out.println(gson.toJson(expenses));
                }
                case 7 -> {
                    Set<PrintIncomeDto> incomes = incomeService.getIncomes();
                    System.out.println(gson.toJson(incomes));
                }
                case 8 -> {
                    String balance = summaryService.getBalance();
                    System.out.println(balance +" zł");
                }
                case 9 -> {
                    SummaryExtendDtos summaryExtendDtos = summaryService.summaryExtendDtos();
                    System.out.println(gson.toJson(summaryExtendDtos));
                }
                case 10 -> {
                    System.out.println("Type start and end date in format yyyy-mm-dd");
                    System.out.println("Start date: ");
                    String startDate = in.next();
                    System.out.println("End date: ");
                    String endDate = in.next();
                    Set<PrintExpenseDto> expensesBetweenDate = expenseService.getExpensesBetweenDate(startDate, endDate);
                    System.out.println(gson.toJson(expensesBetweenDate));
                }
                case 11 -> {
                    System.out.println("Type category name: ");
                    in.nextLine();
                    String categoryName = in.nextLine();
                    categoryService.addCategory(categoryName);
                }
                case 12 -> {
                    System.out.println("Type category name: ");
                    in.nextLine();
                    String categoryName = in.nextLine();
                    try {
                        categoryService.deleteCategory(categoryName);
                    } catch (ApplicationException applicationException) {
                        System.err.println(applicationException.getMessage());
                    }
                }
            }
        }
    }

}
