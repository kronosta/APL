package APL.types;

import APL.types.functions.Mop;
import APL.types.arrs.SingleItemArr;

public class BoxedMop extends Primitive {

  public Mop mop;

  public BoxedMop(Mop mop) {
    this.mop = mop;
  }

  @Override
  public Value ofShape(int[] sh) {
    return SingleItemArr.maybe(this, sh);
  }

  public String toString() {
    return "◫" + mop.toString();
  }

}
