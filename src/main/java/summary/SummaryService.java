package summary;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import config.ConnectionManager;
import expanse.Expense;
import expanse.ExpenseDao;
import expanse.PrintExpenseDto;
import income.Income;
import income.IncomeDao;
import income.PrintIncomeDto;
import jakarta.persistence.EntityManager;

public class SummaryService {

    private ExpenseDao expenseDao;
    private IncomeDao incomeDao;

    public SummaryService(ExpenseDao expenseDao, IncomeDao incomeDao) {
        this.expenseDao = expenseDao;
        this.incomeDao = incomeDao;
    }

    public SummaryDto getSummary() {
        List<Expense> expenses = expenseDao.findAll();
        List<Income> incomes = incomeDao.findAll();
        Set<PrintExpenseDto> expenseDtos = expenses.stream()
                                                   .map(e -> new PrintExpenseDto(e.getId(), e.getAmount().toString() + " zł",e.getComment(), e.getCategory().getName(),
                                                                                 e.getExpanseAddDate().toString()))
                                                   .collect(Collectors.toSet());
        Set<PrintIncomeDto> incomeDtos = incomes.stream()
                                                .map(i -> new PrintIncomeDto(i.getId(), i.getAmount().toString() + " zł", i.getComment(), i.getIncomeAddDate().toString()))
                                                .collect(Collectors.toSet());
        SummaryDto summaryDto = new SummaryDto(incomeDtos, expenseDtos);
        return summaryDto;
    }

    public String getBalance() {
        List<Expense> expenses = expenseDao.findAll();
        List<Income> incomes = incomeDao.findAll();
        BigDecimal expensesBalance = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeBalance = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balance = incomeBalance.subtract(expensesBalance);
        return balance.toString();
    }

    public SummaryExtendDtos summaryExtendDtos() {
        List<Object[]> expenseGroupByCategoryList = expenseDao.findExpenseSummaryGroupByCategory();
        SummaryExtendDtos summaryExtendDtos = new SummaryExtendDtos();
        for (Object[] objects : expenseGroupByCategoryList) {
            BigDecimal totalCost = (BigDecimal) objects[0];
            String categoryName = (String) objects[1];
            Long numberOfExpenses = (Long) objects[2];
            SummaryExpanseDto summaryExpanseDto = new SummaryExpanseDto(categoryName, totalCost, numberOfExpenses);
            summaryExtendDtos.getSummaryExpanseDtoList().add(summaryExpanseDto);
        }

        return summaryExtendDtos;
    }

}
