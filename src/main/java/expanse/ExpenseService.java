package expanse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import category.Category;
import category.CategoryDao;

public class ExpenseService {
    private ExpenseDao expenseDao;
    private CategoryDao categoryDao;

    public ExpenseService(ExpenseDao expenseDao, CategoryDao categoryDao) {
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
    }

    public void addExpense(ExpenseDto expenseDto) {
        Category category = categoryDao.findByName(expenseDto.getCategory());
        Expense expense = new Expense(expenseDto.getAmount(), expenseDto.getComment(), category);
        expenseDao.insert(expense);
    }

    public Set<PrintExpenseDto> getExpensesBetweenDate(String startDate, String endDate) {
        String[] splitStartDate = startDate.split("-");
        LocalDate startLocalDate = LocalDate.of(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]), Integer.parseInt(splitStartDate[2]));
        String[] splitEndDate = endDate.split("-");
        LocalDate endLocalDate = LocalDate.of(Integer.parseInt(splitEndDate[0]), Integer.parseInt(splitEndDate[1]), Integer.parseInt(splitEndDate[2]));

        List<Expense> expenses = expenseDao.findBetweenDates(startLocalDate, endLocalDate);

        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString()))
                       .collect(Collectors.toSet());
    }

    public Set<PrintExpenseDto> getExpenses() {
        List<Expense> expenses = expenseDao.findAll();
        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString()))
                       .collect(Collectors.toSet());
    }

    public void deleteExpense(Long expensedToBeDeleted) {
        if (expensedToBeDeleted != null) {
           expenseDao.deleteById(expensedToBeDeleted);
        }
    }
}
