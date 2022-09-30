package income;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import validation.ValidationMessage;

public class IncomeService {

    private IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public void addIncome(IncomeDto incomeDto) throws IllegalArgumentException {
        ValidationMessage validationMessage = validateIncomeDto(incomeDto);
        if(validationMessage.isValidationResult()) {
            Income income = new Income(incomeDto.getAmount(), incomeDto.getComment());
            incomeRepository.insert(income);
        } else {
            throw new IllegalArgumentException(validationMessage.getMessage());
        }
    }

    public void deleteIncome(@NotNull Long id) {
        Income income = incomeRepository.find(id);
        if (income != null) {
            incomeRepository.delete(income);
        }
    }

    public Set<PrintIncomeDto> getIncomes() {
        List<Income> incomes = incomeRepository.findAll();
        return incomes.stream()
                      .map(income -> new PrintIncomeDto(income.getId(), income.getAmount().toString() + " zł", income.getComment(), income.getIncomeAddDate().toString()))
                      .collect(Collectors.toSet());
    }

    public ValidationMessage validateIncomeDto(IncomeDto incomeDto) {
        ValidationMessage validationMessage = new ValidationMessage();
        validationMessage.setValidationResult(true);
        if (incomeDto == null || incomeDto.getAmount() == null) {
            validationMessage.setValidationResult(false);
            validationMessage.setMessage(ValidationMessage.MISSING_FIELDS);
        }
        return validationMessage;
    }
}
