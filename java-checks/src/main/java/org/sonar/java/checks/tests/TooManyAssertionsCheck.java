/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks.tests;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ExpressionStatementTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "S5961")
public class TooManyAssertionsCheck extends IssuableSubscriptionVisitor {

  private static final int DEFAULT_MAX = 10;

  @RuleProperty(description = "The maximum authorized number of assertions in a test method", defaultValue = "" + DEFAULT_MAX)
  public int maximumAssertionNumber = DEFAULT_MAX;

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodTree method = (MethodTree) tree;
    List<StatementTree> statementTrees = method.block().body();
    long numberOfAsserts = statementTrees.stream()
      .filter(statementTree -> statementTree.is(Tree.Kind.EXPRESSION_STATEMENT))
      .map(statementTree -> ((ExpressionStatementTree) statementTree).expression())
      .filter(expr -> expr.is(Tree.Kind.METHOD_INVOCATION))
      .map(expr -> ((MethodInvocationTree) expr).methodSelect())
      .filter(methodSelect -> methodSelect.is(Tree.Kind.IDENTIFIER))
      .map(methodSelect -> ((IdentifierTree) methodSelect).name())
      .filter(name -> name.startsWith("assert"))
      .count();

    if (numberOfAsserts > maximumAssertionNumber) {
      reportIssue(method.simpleName(), String.format("Refactor this method in order to have less than %d assertions.", maximumAssertionNumber));
    }
  }
}
