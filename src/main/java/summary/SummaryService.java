package summary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import expanse.Expense;
import expanse.ExpenseRepository;
import expanse.PrintExpenseDto;
import income.Income;
import income.IncomeRepository;
import income.PrintIncomeDto;

public class SummaryService {

    private ExpenseRepository expenseRepository;
    private IncomeRepository incomeRepository;

    public SummaryService(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    public SummaryDto getSummary() {
        List<Expense> expenses = expenseRepository.findAll();
        List<Income> incomes = incomeRepository.findAll();
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
        List<Expense> expenses = expenseRepository.findAll();
        List<Income> incomes = incomeRepository.findAll();
        BigDecimal expensesBalance = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomeBalance = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balance = incomeBalance.subtract(expensesBalance);
        return balance.toString();
    }

    public SummaryExtendDtos summaryExtendDtos() {
        List<Object[]> expenseGroupByCategoryList = expenseRepository.findExpenseSummaryGroupByCategory();
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
