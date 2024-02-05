

package com.dws.challenge.model;



import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
public class TransferMoneyDetails {
  @NotNull
  @NotEmpty
  private String accountFromId;

  @NotNull
  @NotEmpty
  private String accountToId;

  @NotNull
  private BigDecimal amount;

}

