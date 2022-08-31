package summary;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryExpanseDto {
    private String categoryName;
    private BigDecimal totalAmount;
    private Long numberOfExpanse;
}
