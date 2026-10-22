package be.sennegeerts.kassa_app_api.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank @Size(max = 100) String name,
    @NotNull @DecimalMin("0.00") @Digits(integer = 8, fraction = 2) BigDecimal price,
    Integer sortOrder          // Integer: null = auto (last)
) {}