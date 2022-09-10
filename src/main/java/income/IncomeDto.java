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

    public IncomeDto(BigDecimal amount, String comment) {
        this.amount = amount;
        this.comment = comment;
    }
}
