

package com.dws.challenge.model;



import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Data
public class TransferAccountsDetails {
  @NotNull
  @NotEmpty
  private String from;

  @NotNull
  @NotEmpty
  private String to;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal amount;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TransferAccountsDetails that = (TransferAccountsDetails) o;
    return from.equals(that.from) && to.equals(that.to) && amount.equals(that.amount);
  }


  @Override
  public int hashCode() {
    return Objects.hash(from, to, amount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TransferAccountsDetails class{\n");
    
    sb.append("    from: ").append(from).append("\n");
    sb.append("    to: ").append(to).append("\n");
    sb.append("    amount: ").append(amount).append("\n");
    sb.append("}");
    return sb.toString();
  }



}

