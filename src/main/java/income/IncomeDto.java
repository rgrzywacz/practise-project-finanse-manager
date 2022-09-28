package income;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class IncomeDto {
    private Long id;
    private BigDecimal amount;
    private String comment;

    private String accountNumber;

    public IncomeDto(BigDecimal amount, String comment, String accountNumber) {
        this.amount = amount;
        this.comment = comment;
        this.accountNumber = accountNumber;
    }
}
