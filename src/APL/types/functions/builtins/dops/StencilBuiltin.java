package APL.types.functions.builtins.dops;

import APL.Scope;
import APL.errors.DomainError;
import APL.errors.RankError;
import APL.errors.SyntaxError;
import APL.types.Arr;
import APL.types.Num;
import APL.types.Obj;
import APL.types.Value;
import APL.types.functions.DerivedDop;
import APL.types.functions.Dop;

public class StencilBuiltin extends Dop {
    @Override public String repr() {
        return "⌺";
    }

    public StencilBuiltin(Scope sc) {
        super(sc);
    }

    public Value call(Obj aa, Obj ww, Value w, DerivedDop derv) {
        /*
        if (!(ww instanceof Arr)) throw new SyntaxError("Right operator argument to Stencil must be an array.", this);
        if (((Arr)ww).rank != 2) throw new RankError("Right operator argument to Stencil must have rank 2.", this);
        if (((Arr)ww).shape[0] != 2) throw new DomainError("Right operator argument to Stencil must have 2 rows.", this);
        if (((Arr)ww).shape[1] > w.rank) throw new DomainError("Right operator argument to Stencil must not have more columns than the rank of the right value argument.", this);
        Arr wwa = (Arr)ww;
        if (wwa.shape[1] == w.rank)
        {
            
        }
        */
        return new Num(0);
    }
}
