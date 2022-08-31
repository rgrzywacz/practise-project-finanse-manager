package income;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomeDto {
    private Long id;
    private BigDecimal amount;
    private String comment;

    private LocalDate incomeAddDate;

    public IncomeDto(BigDecimal amount, String comment) {
        this.amount = amount;
        this.comment = comment;
        this.incomeAddDate = LocalDate.now();
    }
}
