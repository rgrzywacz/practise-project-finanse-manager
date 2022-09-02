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

    public static void main(String[] args) {
        ConnectionManager.getEntityManager();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        CategoryDao categoryDao = new CategoryDao();
        CategoryService categoryService = new CategoryService(categoryDao);

        ExpenseDao expenseDao = new ExpenseDao();
        ExpenseService expenseService = new ExpenseService(expenseDao, categoryDao);

        IncomeDao incomeDao = new IncomeDao();
        IncomeService incomeService = new IncomeService(incomeDao);

        SummaryService summaryService = new SummaryService(expenseDao, incomeDao);
        List<String> categories;

        Scanner in = new Scanner(System.in).useLocale(Locale.ROOT);

        while (true) {
            System.out.println("Podaj operacje do wykonania");

            System.out.println("1 - Dodaj wydatek");
            System.out.println("2 - Dodaj przychód");
            System.out.println("3 - Usuń wydatek");
            System.out.println("4 - Usuń przychód");
            System.out.println("5 - Wyświetl wszystkie wydatki i przychody");
            System.out.println("6 - Wyświetl wszystkie wydatki");
            System.out.println("7 - Wyświetl wszystkie przychody");
            System.out.println("8 - Wyświetl saldo");
            System.out.println("9 - Wyświetl wszystkie wydatki na podstawie kategorii");
            System.out.println("10 - Wyświetl wydatki z podanego okresu czasu");
            System.out.println("11 - Dodaj nową kategorię");
            System.out.println("12 - Usuń kategorię");

            int result = in.nextInt();
            switch (result) {
                case 1 -> {
                    categories = categoryService.getAllCategoryNames();
                    System.out.println("Podaj kwotę wydatku: ");
                    BigDecimal totalCost = new BigDecimal(String.valueOf(in.nextBigDecimal())).setScale(2, RoundingMode.CEILING);
                    in.nextLine();
                    System.out.println("Podaj kategorię wydatku: " + categories.toString());
                    String category = in.nextLine();
                    System.out.println("Podaj komentarz opcjonalnie: ");
                    String comment = in.nextLine();
                    ExpenseDto expenseDto = new ExpenseDto(totalCost, comment, category);
                    expenseService.addExpense(expenseDto);
                }
                case 2 -> {
                    System.out.println("Podaj kwotę przychodu: ");
                    BigDecimal totalCost = new BigDecimal(String.valueOf(in.nextBigDecimal()));
                    System.out.println("Podaj komentarz: ");
                    String comment = in.next();
                    IncomeDto incomeDto = new IncomeDto(totalCost, comment);
                    try {
                        incomeService.addIncome(incomeDto);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.println("Podaj id wydatku, który ma zostać usunięty: ");
                    Long expensedToBeDeleted = in.nextLong();
                    expenseService.deleteExpense(expensedToBeDeleted);
                }
                case 4 -> {
                    System.out.println("Podaj id przychodu, który ma zostać usunięty: ");
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
                    System.out.println("Podaj datę początkową i końcową w formacie yyyy-mm-dd");
                    System.out.println("Data początkowa: ");
                    String startDate = in.next();
                    System.out.println("Data końcowa: ");
                    String endDate = in.next();
                    Set<PrintExpenseDto> expensesBetweenDate = expenseService.getExpensesBetweenDate(startDate, endDate);
                    System.out.println(gson.toJson(expensesBetweenDate));
                }
                case 11 -> {
                    System.out.println("Podaj nazwę kategorii: ");
                    in.nextLine();
                    String categoryName = in.nextLine();
                    categoryService.addCategory(categoryName);
                }
                case 12 -> {
                    System.out.println("Podaj nazwę kategorii: ");
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
