package APL.types.functions.builtins.dops;

import APL.Scope;
import APL.errors.DomainError;
import APL.errors.RankError;
import APL.errors.SyntaxError;
import APL.types.Arr;
import APL.types.Fun;
import APL.types.Num;
import APL.types.Obj;
import APL.types.Value;
import APL.types.functions.DerivedDop;
import APL.types.functions.Dop;
import java.util.ArrayList;
import java.util.Arrays;

public class StencilBuiltin extends Dop {
    @Override public String repr() {
        return "⌺";
    }

    public StencilBuiltin(Scope sc) {
        super(sc);
    }

    public Value call(Obj aa, Obj ww, Value w, DerivedDop derv) {
        if (!(ww instanceof Arr)) throw new SyntaxError("Right operator argument to Stencil must be an array.", this);
        if (((Arr)ww).rank != 2) throw new RankError("Right operator argument to Stencil must have rank 2.", this);
        if (((Arr)ww).shape[0] != 2) throw new DomainError("Right operator argument to Stencil must have 2 rows.", this);
        if (((Arr)ww).shape[1] > w.rank) throw new DomainError("Right operator argument to Stencil must not have more columns than the rank of the right value argument.", this);
        if (!(aa instanceof Fun)) throw new SyntaxError("Left operator argument to Stencil must be a function.", this);
        Arr wwa = (Arr)ww;
        Fun aaf = (Fun)aa;
        int[] subShape = Value.subArray(wwa, new int[]{0, 0}, new int[]{1, wwa.shape[1]}, this).asIntArrClone();
        for (int i = 0; i < w.rank; i++)
            if (subShape[i] > w.shape[i] + 1) throw new DomainError("Right operator argument to Stencil must not specify a dimension larger than (the same dimension of w) plus 1.", this); 
        int[] subMove = Value.subArray(wwa, new int[]{1, 0}, new int[]{1, wwa.shape[1]}, this).asIntArrClone();
        boolean[] mismatchRank = new boolean[w.rank];
        if (subShape.length < w.rank)
        {
            for (int i = subShape.length; i < w.rank; i++)
                mismatchRank[i] = true;
            int[] bigSubShape = new int[w.rank];
            System.arraycopy(w.shape, 0, bigSubShape, 0, w.rank);
            System.arraycopy(subShape, 0, bigSubShape, 0, subShape.length);
            subShape = bigSubShape;
            int[] bigSubMove = new int[w.rank];
            System.arraycopy(subMove, 0, bigSubMove, 0, subMove.length);
            subMove = bigSubMove;
        }
        boolean done = false;
        int[] centerCoords = new int[w.rank]; //will start out all zeroes;
        int[] originalCenterCoords = new int[w.rank];
            System.arraycopy(centerCoords, 0, originalCenterCoords, 0, w.rank);
        //Space behind the center of each array, NOT counting the adjacent element if the dimension is even.
        int[] topLeftSpace = new int[w.rank];
        //Space in front the center of each array, counting the adjacent element if the dimension is even.
        int[] bottomRightSpace = new int[w.rank];
        for (int i = 0; i < w.rank; i++)
        {
            topLeftSpace[i] = (subShape[i] / 2 - ((subShape[i] % 2 == 0) ? 1 : 0));
            if (mismatchRank[i]) topLeftSpace[i]--;
        }
        for (int i = 0; i < w.rank; i++)
        {
            bottomRightSpace[i] = (subShape[i] / 2 + ((subShape[i] % 2 == 0) ? 1 : 0));
            if (mismatchRank[i]) bottomRightSpace[i]++;
        }
        int[] resultCoords = new int[w.rank];
        boolean[] resultCoordsLock = new boolean[w.rank];
        ArrayList<Value> singleResults = new ArrayList<>();
        while (!done)
        {
            int[] topLeftCoords = new int[w.rank];
            for (int i = 0; i < w.rank; i++)
            {
                topLeftCoords[i] = centerCoords[i] - topLeftSpace[i];
                if (topLeftCoords[i] < 0) topLeftCoords[i] = 0;
            }
            int[] bottomRightCoords = new int[w.rank];
            for (int i = 0; i < w.rank; i++)
            {
                bottomRightCoords[i] = centerCoords[i] + bottomRightSpace[i];
                if (bottomRightCoords[i] >= w.shape[i]) bottomRightCoords[i] = w.shape[i] - 1;
            }
            int[] indexShape = new int[w.rank];
            for (int i = 0; i < w.rank; i++)
                indexShape[i] = bottomRightCoords[i] - topLeftCoords[i] + 1;
            int totalNumElements = 1;
            for (int i = 0; i < w.rank; i++)
                totalNumElements *= subShape[i];
            Value[] copyInto = new Value[totalNumElements];
            int[] coords = new int[w.rank];
            for (int i = 0; i < w.rank; i++)
                coords[i] = centerCoords[i] - topLeftSpace[i];
            int[] originalCoords = new int[w.rank];
            System.arraycopy(coords, 0, originalCoords, 0, w.rank);
            for (int i = 0; i < totalNumElements; i++)
            {
                boolean coordsValid = true;
                for (int j = 0; j < w.rank; j++)
                    coordsValid = coordsValid && coords[j] >= 0 && coords[j] < w.shape[j];
                if (coordsValid)
                    copyInto[i] = w.simpleAt(coords);
                else
                    copyInto[i] = new Num(0);
                boolean incDone = false;
                int posIndex = w.rank - 1;
                while (!incDone)
                {
                    coords[posIndex]++;
                    if (coords[posIndex] >= (originalCoords[posIndex] + subShape[posIndex]))
                    {
                        coords[posIndex] = originalCoords[posIndex];
                        posIndex--;
                        if (posIndex < 0)
                        {
                            incDone = true;
                        }
                    }
                    else incDone = true;
                }
            }
            int[] fillElementNum = new int[w.rank];
            for (int i = 0; i < w.rank; i++)
            {
                fillElementNum[i] = subShape[i] - indexShape[i];
                if (bottomRightCoords[i] < (centerCoords[i] + bottomRightSpace[i]))
                    fillElementNum[i] = -(fillElementNum[i]);
                else if (centerCoords[i] == 0 && subShape[i] % 2 == 0)
                    fillElementNum[i]++;
            }
            Value[] fillElementNumValues = new Value[w.rank];
            for (int i = 0; i < w.rank; i++)
                fillElementNumValues[i] = new Num(fillElementNum[i]);
            singleResults.add(aaf.call(Arr.create(fillElementNumValues), Arr.create(copyInto, subShape)));
            boolean incDone = false;
            int posIndex = w.rank - 1;
            while (!incDone)
            {
                centerCoords[posIndex] += subMove[posIndex];
                if (!resultCoordsLock[posIndex]) resultCoords[posIndex]++;
                if (centerCoords[posIndex] >= w.shape[posIndex] - (subShape[posIndex] % 2 == 0 ? 1 : 0) || subMove[posIndex] == 0)
                {
                    centerCoords[posIndex] = originalCenterCoords[posIndex];
                    resultCoordsLock[posIndex] = true;
                    posIndex--;
                    if (posIndex < 0)
                    {
                        incDone = true;
                        done = true;
                    }
                }
                else incDone = true;
            }
        }
        //After the loop, resultCoords should be the dimensions of the combined result array.
        return Arr.create(singleResults, resultCoords);
    }
}
