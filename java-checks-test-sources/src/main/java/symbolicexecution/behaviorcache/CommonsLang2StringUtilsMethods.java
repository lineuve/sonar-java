package symbolicexecution.behaviorcache;

import org.apache.commons.lang.StringUtils;

class StringUtilsMethodsLang2 {
  void isEmpty() {
    String param = null;
    if(!StringUtils.isEmpty(param)) {
      param.toString();
    }
  }

  void isNotEmpty() {
    String param = null;
    if(StringUtils.isNotEmpty(param)) {
      param.toString();
    }
  }

  void isBlank() {
    String param = null;
    if(!StringUtils.isBlank(param)) {
      param.toString();
    }
  }

  void isNotBlank() {
    String param = null;
    if(StringUtils.isNotBlank(param)) {
      param.toString();
    }
  }

  void otherMethod() {
    String param = "";
    StringUtils.chomp(param);
  }
}
