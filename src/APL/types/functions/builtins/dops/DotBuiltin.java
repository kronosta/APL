package APL.types.functions.builtins.dops;

import APL.types.*;
import APL.types.functions.*;
import APL.types.functions.builtins.mops.EachBuiltin;
import APL.types.functions.builtins.mops.ReduceBuiltin;
import APL.errors.DomainError;

public class DotBuiltin extends Dop {
  @Override public String repr() {
    return ".";
  }
  
  public Value call(Obj aa, Obj ww, Value a, Value w, DerivedDop derv) {
    Fun wwf = isFn(ww, '⍹');
    if (a.rank == 1 && w.rank == 1) {
      return new ReduceBuiltin().derive(aa).call(new EachBuiltin().derive(wwf).call(a, w)); // TODO not lazy
    }
    else if (a.rank == 2 && w.rank == 2) {
      if (a.shape[1] != w.shape[0])
        throw new DomainError("Dot builtin passed two matrices as value arguments without matching rows and columns.", this);
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
    else if (a.rank == 0 || w.rank == 0) {
      throw new DomainError("Passed scalar as value argument to Inner Product builtin.", this);
    }
    else {
      if (a.shape[a.rank - 1] != w.shape[0])
        throw new DomainError("Inner Product builtin passed two arrays as value arguments without matching rows and columns.", this);
      int aCondensedShape = 1;
      int wCondensedShape = 1;
      for (int i = 0; i < a.rank - 1; i++)
        aCondensedShape *= a.shape[i];
      for (int i = 1; i < w.rank; i++)
        wCondensedShape *= w.shape[i];
      Value aCondensed = a.ofShape(new int[]{aCondensedShape, a.shape[a.rank - 1]});
      Value wCondensed = w.ofShape(new int[]{w.shape[0], wCondensedShape});
      Value resultMatrix = this.call(aa, ww, aCondensed, wCondensed, derv);
      int[] resultShape = new int[a.rank + w.rank - 2];
      for (int i = 0; i < a.rank - 1; i++)
        resultShape[i] = a.shape[i];
      for (int i = 1; i < w.rank; i++)
        resultShape[a.rank - 2 + i] = w.shape[i];
      return resultMatrix.ofShape(resultShape);
    }
  }
}