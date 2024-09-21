// =================================================================================================================================
/**
 * An operator which, when applied to operands, yeilds a value.
 */
abstract public class Operator {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The position in the source at which this operator appears. */
    public final int   _position;

    /** The token that specified this operator. */
    public final Token _token;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create an operator.
     *
     * @param token The token that specifies this operator.
     */
    public Operator (Token token) {

	_position = token._position;
	_token    = token;

    } // Operator ()
    // =============================================================================================================================



    // =============================================================================================================================
    public String toString () {

	return _token._text;

    } // toString ()
    // =============================================================================================================================
    
    public static class Plus extends Operator {
        public Plus(Token token) {
            super(new Token(token._position, Token.Type.PLUS));
        }
    }

    public static class Subtract extends Operator {
        public Subtract(Token token) {
            super(new Token(token._position, Token.Type.DASH));
        }
    }

    public static class Multiply extends Operator {
        public Multiply(Token token) {
            super(new Token(token._position, Token.Type.STAR));
        }
    }

    public static class Divide extends Operator {
        public Divide(Token token) {
            super(new Token(token._position, Token.Type.SLASH));
        }
    }

    public static class Remainder extends Operator {
        public Remainder(Token token) {
            super(new Token(token._position, Token.Type.PERCENT));
        }
    }


// =================================================================================================================================
} // class Operator
// =================================================================================================================================
