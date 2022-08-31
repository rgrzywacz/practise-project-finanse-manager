package expanse;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseDto {
    private Long id;
    private BigDecimal amount;
    private String comment;
    private String category;

    private LocalDate expanseAddDate;

    public ExpenseDto(BigDecimal amount, String comment, String category) {
        this.amount = amount;
        this.comment = comment;
        this.category = category;
        this.expanseAddDate = LocalDate.now();
    }
}
