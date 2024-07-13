package com.zerobase.used_trade.repository.custom.contributor;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {

  @Override
  public void contributeFunctions(FunctionContributions functionContributions) {
    functionContributions.getFunctionRegistry()
        .register("replace", new StandardSQLFunction("replace", StandardBasicTypes.STRING));
  }
}
