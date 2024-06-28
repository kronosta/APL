package APL.types;

import APL.types.functions.Dop;
import APL.types.arrs.SingleItemArr;

public class BoxedDop extends Primitive {

  public Dop dop;

  public BoxedDop(Dop dop) {
    this.dop = dop;
  }

  @Override
  public Value ofShape(int[] sh) {
    return SingleItemArr.maybe(this, sh);
  }

  public String toString() {
    return "◫" + dop.toString();
  }

}
