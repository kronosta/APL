package APL.types.functions.builtins.dops;

import APL.types.*;
import APL.types.functions.*;
import APL.types.functions.builtins.mops.ReduceBuiltin;
import APL.errors.DomainError;
import APL.errors.SyntaxError;

public class DotBuiltin extends Dop {
  @Override public String repr() {
    return ".";
  }
  
  public Value call(Obj aa, Obj ww, Value a, Value w, DerivedDop derv) {
    Fun wwf = isFn(ww, '⍹');
    if (a.rank == 1 && w.rank == 1) {
      return new ReduceBuiltin().derive(aa).call(wwf.call(a, w)); // TODO not lazy
    }
    else if (a.rank == 2 && w.rank == 2) {
      if (a.shape[1] != w.shape[0])
        throw new DomainError("Dot builtin passed two matrices without matching rows and columns.", this);
      Value[] results = new Value[a.shape[0] * w.shape[1]];
      for (int i = 0; i < a.shape[0]; i++)
      {
        for (int j = 0; j < w.shape[1]; j++)
        {
          Value[] row = new Value[a.shape[1]];
          for (int k = 0; k < a.shape[1]; k++)
          {
            row[k] = a.simpleAt(new int[]{i, k});
          }
          Value[] col = new Value[w.shape[0]];
          for (int k = 0; k < w.shape[0]; k++)
          {
            col[k] = w.simpleAt(new int[]{k, j});
          }
          results[(i * w.shape[1]) + j] = this.call(aa, ww, Arr.create(row), Arr.create(col), derv);
        }
      }
      return Arr.create(results, new int[]{a.shape[0], w.shape[1]});
    }
    throw new DomainError("Dot builtin takes only matrices and vectors, passed ranks "+a.rank+", "+w.rank, this);
  }
}