package expanse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import category.Category;
import category.CategoryRepository;
import validation.ValidationMessage;

public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public void addExpense(ExpenseDto expenseDto) throws IllegalArgumentException {
        Category category = categoryRepository.findByName(expenseDto.getCategory());
        ValidationMessage validationMessage = validateExpenseDto(expenseDto, category);
        if (validationMessage.isValidationResult()) {
            Expense expense = new Expense(expenseDto.getAmount(), expenseDto.getComment(), category);
            expenseRepository.insert(expense);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }

    }

    public Set<PrintExpenseDto> getExpensesBetweenDate(String startDate, String endDate) {
        String[] splitStartDate = startDate.split("-");
        LocalDate startLocalDate = LocalDate.of(Integer.parseInt(splitStartDate[0]), Integer.parseInt(splitStartDate[1]), Integer.parseInt(splitStartDate[2]));
        String[] splitEndDate = endDate.split("-");
        LocalDate endLocalDate = LocalDate.of(Integer.parseInt(splitEndDate[0]), Integer.parseInt(splitEndDate[1]), Integer.parseInt(splitEndDate[2]));

        List<Expense> expenses = expenseRepository.findBetweenDates(startLocalDate, endLocalDate);

        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł", expense.getCategory().getName(), expense.getComment(),
                                                           expense.getExpanseAddDate().toString()))
                       .collect(Collectors.toSet());
    }

    public Set<PrintExpenseDto> getExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                       .map(expense -> new PrintExpenseDto(expense.getId(), expense.getAmount().toString() + " zł",expense.getComment(), expense.getCategory().getName(),
                                                           expense.getExpanseAddDate().toString()))
                       .collect(Collectors.toSet());
    }

    public void deleteExpense(Long expensedToBeDeleted) {
        if (expensedToBeDeleted != null) {
           expenseRepository.deleteById(expensedToBeDeleted);
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
}
