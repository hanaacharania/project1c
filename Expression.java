// =================================================================================================================================
/**
 * An expression which, when evaluated, yields some value.
 */
abstract public class Expression {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The position in the source code at which the expression begins. */
    public final int _position;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a expression.  The type is not provided because it is assumed to be unknown (pending binding).
     *
     * @param position The location in the source code at which the expression begins.
     */
    public Expression (int position) {
        _position = position;
    } // Expression ()

    public abstract String toAssembly();
    // =============================================================================================================================

    public static class IntegerLiteral extends Expression {
        
        public final int value;
        public IntegerLiteral(Token token) {
            super(token._position);
            if (token._type != Token.Type.INTEGER) {
                throw new IllegalArgumentException("Token type must be an integer");
            }
            value = Integer.parseInt(token._text);
        }

        @Override
        public String toString() {
            return "IntegerLiteral: " + value;

        }

        @Override
        public String toAssembly() {
            return "\tmov\trax, " + value + "\n";
        }
    }

    public static class Operation extends Expression {
        public final Expression left;
        public final String operator;
        public final Expression right;

        public Operation(int position, Expression left, String operator, Expression right) {
            super(position);
            this.left = left;
            this.operator = operator;
            this.right = right; }

    
    @Override
    public String toString () {
        return "(" + operator + left + " " + right + ")";
        } // toString ()

        @Override
        public String toAssembly() {
            String assembly = "";
            assembly += left.toAssembly();
            assembly += "\tpush\trax\n";

            assembly += right.toAssembly();
            assembly += "\tpush\trbx\n";

            switch (operator) {
                case "+":
                    assembly += "\tpop\trbx\n";
                    assembly += "\tpop\trax\n";
                    assembly += "\tadd\trax, rbx\n";
                    break;
                case "-":
                    assembly += "\tpop\trbx\n";
                    assembly += "\tpop\trax\n";
                    assembly += "\tsub\trax, rbx\n";
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + operator);
            }
        return assembly;
        }

    }



// =================================================================================================================================
} // class Expression
// =================================================================================================================================
