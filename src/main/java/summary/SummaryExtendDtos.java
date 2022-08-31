package summary;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SummaryExtendDtos {
    private List<SummaryExpanseDto> summaryExpanseDtoList = new ArrayList<>();
}
