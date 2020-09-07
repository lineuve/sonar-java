package checks;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class TooManyAssertionsCheckCustom2 {

  @Test
  void test1() { // Noncompliant [[sc=8;ec=13]]{{Refactor this method in order to have less than 2 assertions.}}
    assertEquals(1, f(1));
    assertEquals(2, f(2));
    assertEquals(3, f(3));
  }

  @Test
  void test2() { // Compliant
    assertEquals(2, f(2));
    assertEquals(3, f(1));
  }

  int f(int x) {
    return x;
  }

}
