package APL.types;

import APL.types.Fun;
import APL.types.arrs.SingleItemArr;

@SuppressWarnings("unused")
public class BoxedFun extends Primitive {

  public Fun fun;

  public BoxedFun(Fun fun) {
    this.fun = fun;
  }

  @Override
  public Value ofShape(int[] sh) {
    return SingleItemArr.maybe(this, sh);
  }

  public String toString() {
    return "◫" + fun.toString();
  }

}
