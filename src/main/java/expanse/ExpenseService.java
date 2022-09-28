package expanse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import account.Account;
import account.AccountDao;
import account.AccountService;
import category.Category;
import category.CategoryDao;
import validation.ValidationMessage;

public class ExpenseService {
    private final ExpenseDao expenseDao;
    private final CategoryDao categoryDao;
    private final AccountDao accountDao;

    public ExpenseService(ExpenseDao expenseDao, CategoryDao categoryDao, AccountDao accountDao) {
        this.expenseDao = expenseDao;
        this.categoryDao = categoryDao;
        this.accountDao = accountDao;
    }

    public void addExpense(ExpenseDto expenseDto) throws IllegalArgumentException {
        Category category = categoryDao.findByName(expenseDto.getCategory());
        Account byAccountNumber = accountDao.findByAccountNumber(expenseDto.getAccountNumber());
        ValidationMessage validationMessage = validateExpenseDto(expenseDto, category);
        if (validationMessage.isValidationResult()) {
            Expense expense = new Expense(expenseDto.getAmount(), expenseDto.getComment(), category, byAccountNumber);
            expenseDao.insert(expense);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }

    }

    public Set<PrintExpenseDto> getExpensesBetweenDate(String startDate, String endDate) {
        String[] splitStartDate = startDate.split("-");
        LocalDate startLocalDate = LocalDate.of(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]), Integer.parseInt(splitStartDate[2]));
        String[] splitEndDate = endDate.split("-");
        LocalDate endLocalDate = LocalDate.of(Integer.parseInt(splitEndDate[0]), Integer.parseInt(splitEndDate[1]), Integer.parseInt(splitEndDate[2]));

        List<Expense> expenses = expenseDao.findBetweenDates(startLocalDate, endLocalDate);

        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString(), expense.getAccount().getAccountNumber()))
                       .collect(Collectors.toSet());
    }

    public Set<PrintExpenseDto> getExpenses() {
        List<Expense> expenses = expenseDao.findAll();
        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString(), expense.getAccount().getAccountNumber()))
                       .collect(Collectors.toSet());
    }

    public Set<PrintExpenseDto> getExpensesByAccountId(long accountId) {
        List<Expense> expenses = expenseDao.findAllByAccountNumber(accountId);
        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString(), expense.getAccount().getAccountNumber()))
                       .collect(Collectors.toSet());
    }


    public void deleteExpense(Long expensedToBeDeleted) {
        if (expensedToBeDeleted != null) {
           expenseDao.deleteById(expensedToBeDeleted);
        }
    }

    private ValidationMessage validateExpenseDto(ExpenseDto expenseDto, Category category) {
        ValidationMessage validationMessage = new ValidationMessage(ValidationMessage.OK, true);
        if (expenseDto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            validationMessage.setMessage(ValidationMessage.AMOUNT_LOWER_THAN_0);
            validationMessage.setValidationResult(false);
        } else if (category == null) {
            validationMessage.setMessage(ValidationMessage.MISSING_CATEGORY);
            validationMessage.setValidationResult(false);
        }
        return validationMessage;
    }

    public Set<PrintExpenseDto> getExpensesBetweenDateForAccount(String startDate, String endDate, long accountId) {
        String[] splitStartDate = startDate.split("-");
        LocalDate startLocalDate = LocalDate.of(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]), Integer.parseInt(splitStartDate[2]));
        String[] splitEndDate = endDate.split("-");
        LocalDate endLocalDate = LocalDate.of(Integer.parseInt(splitEndDate[0]), Integer.parseInt(splitEndDate[1]), Integer.parseInt(splitEndDate[2]));

        List<Expense> expenses = expenseDao.findBetweenDatesForAccount(startLocalDate, endLocalDate, accountId);

        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString(), expense.getAccount().getAccountNumber()))
                       .collect(Collectors.toSet());
    }
}
