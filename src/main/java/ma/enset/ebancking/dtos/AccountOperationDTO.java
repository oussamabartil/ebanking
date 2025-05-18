package ma.enset.ebancking.dtos;

import lombok.Data;
import ma.enset.ebancking.enumes.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}