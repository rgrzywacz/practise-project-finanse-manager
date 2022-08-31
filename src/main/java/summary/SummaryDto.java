package summary;

import java.util.Set;

import expanse.ExpenseDto;
import expanse.PrintExpenseDto;
import income.IncomeDto;
import income.PrintIncomeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryDto {
    private Set<PrintIncomeDto> incomes;
    private Set<PrintExpenseDto> expenses;
}
