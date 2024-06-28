package APL.types.functions.builtins.fns;

import APL.Main;
import APL.Scope;
import APL.errors.DomainError;
import APL.types.BoxedDop;
import APL.types.BoxedFun;
import APL.types.BoxedMop;
import APL.types.Fun;
import APL.types.Num;
import APL.types.Obj;
import APL.types.Value;
import APL.types.functions.Dop;
import APL.types.functions.Mop;
import APL.types.functions.DerivedMop;
import APL.types.functions.builtins.fns.RShoeBuiltin;

@SuppressWarnings("unused")
public class FunValueBuiltin extends Fun {
    public FunValueBuiltin(Scope sc) {
        super(sc);
    }

    public String repr() {
        return "◫";
    }

    public Value call(Value w) {
        Obj evaled = Main.exec(w.asString(), sc);
        if (evaled instanceof Fun) return new BoxedFun((Fun)evaled);
        if (evaled instanceof Mop) return new BoxedMop((Mop)evaled);
        if (evaled instanceof Dop) return new BoxedDop((Dop)evaled);
        throw new DomainError("Right argument to monadic Fun Value should have evaluated to a function, monadic operator, or dyadic operator, instead got "+evaled.humanType(true), this);
    }

    public Value call(Value a, Value w) {
        if (a instanceof Num) {
            Num an = (Num)a;
            RShoeBuiltin disclose = new RShoeBuiltin(sc);
            switch ((int)(an.num)) {
                case 1:
                {
                    //Apply monadic operator
                    Value op = w.get(0);
                    Value aa = w.get(1);
                    if (!(op instanceof BoxedMop)) throw new DomainError("1 (Fun Value) w: w[1] should be a monadic operator value.", this);
                    return new BoxedFun(((BoxedMop)op).mop.derive(aa instanceof BoxedFun ? ((BoxedFun)aa).fun : aa));
                }
                case 2:
                {
                    //Apply dyadic operator
                    Value op = w.get(0);
                    Value aa = w.get(1);
                    Value ww = w.get(2);
                    if (!(op instanceof BoxedDop)) throw new DomainError("2 (Fun Value) w: w[1] should be a dyadic operator value.", this);
                    return new BoxedFun(((BoxedDop)op).dop.derive(
                        aa instanceof BoxedFun ? ((BoxedFun)aa).fun : aa,
                        ww instanceof BoxedFun ? ((BoxedFun)ww).fun : ww));
                }
                case 3:
                {
                    //Apply monadic function
                    Value fun = w.get(0);
                    Value w2 = w.get(1);
                    if (!(fun instanceof BoxedFun)) throw new DomainError("3 (Fun Value) w: w[1] should be a function value.", this);
                    return ((BoxedFun)fun).fun.call(w2);
                }
                case 4:
                {
                    //Apply dyadic function
                    Value fun = w.get(0);
                    Value a2 = w.get(1);
                    Value w2 = w.get(2);
                    if (!(fun instanceof BoxedFun)) throw new DomainError("4 (Fun Value) w: w[1] should be a function value.", this);
                    return ((BoxedFun)fun).fun.call(a2, w2);
                }
                case 5:
                {
                    //Bind dyadic operator
                    final Value op = w.get(0);
                    final Value arg = w.get(1);
                    Value left = w.get(2);
                    if (!(op instanceof BoxedDop)) throw new DomainError("5 (Fun Value) w: w[1] should be a dyadic operator value.", this);
                    
                    int leftBool = left.asInt();
                    if (leftBool != 0) {
                        return new BoxedMop(new Mop() {
                            Dop dop = ((BoxedDop)op).dop;
                            Obj aa = arg instanceof BoxedFun ? ((BoxedFun)arg).fun : arg;
                            
                            public String repr() {
                                return "5◫: "+op.toString()+", "+arg.toString()+", 1";
                            }

                            public Value call(Obj ww, Value a, Value w, DerivedMop derv){
                                return dop.call(aa, ww, a, w, null);
                            }

                            public Value call(Obj ww, Value w, DerivedMop derv){
                                return dop.call(aa, ww, w, null);
                            }
                        });
                    }
                    else {
                        return new BoxedMop(new Mop() {
                            Dop dop = ((BoxedDop)op).dop;
                            Obj ww = arg instanceof BoxedFun ? ((BoxedFun)arg).fun : arg;
                            
                            public String repr() {
                                return "5◫: "+op.toString()+", "+arg.toString()+", 1";
                            }

                            public Value call(Obj aa, Value a, Value w, DerivedMop derv){
                                return dop.call(aa, ww, a, w, null);
                            }

                            public Value call(Obj aa, Value w, DerivedMop derv){
                                return dop.call(aa, ww, w, null);
                            }
                        });
                    }
                }
                case 6:
                {
                    //Compose monadic operator
                    final Value op1 = w.get(0);
                    final Value op2 = w.get(1);
                    if (!(op1 instanceof BoxedMop)) throw new DomainError("4 (Fun Value) w: w[1] should be a monadic operator value.", this);
                    if (!(op2 instanceof BoxedMop)) throw new DomainError("4 (Fun Value) w: w[2] should be a monadic operator value.", this);
                    return new BoxedMop(new Mop() {
                        Mop mop1 = ((BoxedMop)op1).mop;
                        Mop mop2 = ((BoxedMop)op2).mop;

                        public String repr() {
                            return "6◫: "+op1.toString()+", "+op2.toString();
                        }

                        public Value call(Obj aa, Value w, DerivedMop derv){
                            return mop1.derive(mop2.derive(aa)).call(w);
                        }

                        public Value call(Obj aa, Value a, Value w, DerivedMop derv){
                            return mop1.derive(mop2.derive(aa)).call(a, w);
                        }
                    });
                }
                default:
                    throw new DomainError("Invalid left argument to dyadic Fun Value.", this);
            }
        }
        throw new DomainError("Left argument to dyadic Fun Value should be a number.", this);
    }
}
