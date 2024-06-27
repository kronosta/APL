package APL.types.functions.builtins.fns;

import APL.errors.DomainError;
import APL.types.Value;
import APL.types.Fun;

public class SubArrayBuiltin extends Fun {
    public String repr() {
        return "‡";
    }

    public Value call(Value a, Value w)
    {
        if (a.rank != 2) throw new DomainError("Left argument of Subarray must be a matrix.", this);
        if (a.shape[0] != 2) throw new DomainError("Left arguments of Subarray must have 2 rows.", this);
        int[] pos = Value.subArray(a, new int[]{0, 0}, new int[]{1, a.shape[1]}, this).asIntArr();
        int[] shape = Value.subArray(a, new int[]{1, 0}, new int[]{1, a.shape[1]}, this).asIntArr();
        for (int i = 0; i < pos.length; i++) pos[i]--;
        return Value.subArray(w, pos, shape, this);
    }
}
