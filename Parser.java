// =================================================================================================================================
// IMPORTS

import java.util.List;
import java.util.LinkedList;
// =================================================================================================================================



// =================================================================================================================================
/**
 * Perform lexical and syntactic analysis on an input, generating an internal representation of each element parsed.
 */
public class Parser {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The list of tokens to parse. */
    private List<Token>     _tokens;

    /** The position within the token list during the parsing. */
    private int             _index;
    // =============================================================================================================================



    // =============================================================================================================================
    public Parser (List<Token> tokens) {

	_tokens = tokens;
	_index  = -1;

    } // Parser ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     *   <program> ::= <expression list>
     *
     * @return A <code>Program</code>, which contains lists of expressions.
     */
    public Program parse () {

	// Parse declarations, starting at the beginning of the tokens list.
	_index = 0;
	List<Expression> expressions = parseExpressionList();

	// Return the newly constructed program from this list of expressions.
	return new Program(expressions);

    } // parse ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     *   <expression list> ::= <expression> <expression list> | <EMPTY>
     *
     * @return A list of expressions.
     */
    private List<Expression> parseExpressionList () {
    

	// Attempt to parse one expression.
	Expression expression = parseExpression();

	// If there was none, return the empty list of expressions.
	if (expression == null) {
	    return new LinkedList<Expression>();
	}

	// There was an expression, so parse the remaining list of them, and then combine the one with the many to return.
	List<Expression> expressions = parseExpressionList();
	expressions.addFirst(expression);
	return expressions;
	
    } // parseExpressionList ()
    // =============================================================================================================================



    // =============================================================================================================================
    private Expression parseExpression () {

        int startIndex = _index;
        consumeWhitespace();
        if (endOfTokens()) {
            return null;
        }
        Token token = _tokens.get(_index++);
    
        // <integer>
        Expression expression = null;
        if (token._type == Token.Type.INTEGER) {
            return new Expression.IntegerLiteral(token);
        }

        // Check for invalid negative number
        if (token._type == Token.Type.DASH) {
            Token nextToken = _tokens.get(_index++);
        if (nextToken._type == Token.Type.INTEGER) {
            throw new IllegalArgumentException("Syntax error: No Negative Numbers");
        }
        _index--; // put the token back
        }

        // ... | <operation>
        // Reset to starting position before trying to parse differently.
        _index = startIndex;
        return parseOperation();
        
        }
    // =============================================================================================================================



    // =============================================================================================================================
    // YOUR ADDITIONAL PARSING METHODS HERE.

    /**
     * <expression> ::= '(' <operator> <expression> <expression> ')'
     *
     */

    private Expression parseOperation() {

        consumeWhitespace();
        if (endOfTokens()) {
            return null;
        }
        Token openParen = _tokens.get(_index++);
        if (openParen._type != Token.Type.OPENPAREN) {
            _index--; // put the token back
            return null;
        }

        consumeWhitespace();
        if (endOfTokens()) {
            return null;
        }
        Token operatorToken = _tokens.get(_index++);
        Operator operator = null;
        switch (operatorToken._type) {
            case PLUS:
                operator = new Operator.Plus(operatorToken);
                break;
            case DASH:
                operator = new Operator.Subtract(operatorToken);
                break;
            case STAR:
                operator = new Operator.Multiply(operatorToken);
                break;
            case SLASH:
                operator = new Operator.Divide(operatorToken);
                break;
            default:
                _index--; // put the token back
                return null;
        }

        consumeWhitespace();
        Expression left = parseExpression();
        if (left == null) {
            return null;
        }
        consumeWhitespace();
        Expression right = parseExpression();
        if (right == null) {
            return null;
        }

        consumeWhitespace();
        if (endOfTokens()) {
            return null;
        }
        Token closeParen = _tokens.get(_index++);
        if (closeParen._type != Token.Type.CLOSEPAREN) {
            _index--; // put the token back
            return null;
        }

        return new Expression.Operation(openParen._position, left, operator.toString(), right); }



    // =============================================================================================================================
    


    // =============================================================================================================================
    /**
     * Consume whitespace tokens, leaving the position at the first non-whitespace token encountered.
     *
     * @return The sequence of consecutive whitespace tokens consumed.
     */
    private List<Token> consumeWhitespace () {

	// Read whitespace tokens until there are no more or we encounter a non-whitespace token.
	List<Token> whitespaces = new LinkedList<Token>();
	Token       token       = null;
	while ( !endOfTokens() && (token = _tokens.get(_index++)).isWhitespace() ) {
	    whitespaces.addLast(token);
	}

	// Put the final token back into play if we didn't hit the end of the input.
	if (!endOfTokens()) {
	    _index -= 1;
	}

	return whitespaces;

    } // consumeWhitespace ()
    // =============================================================================================================================


    
    
    // =============================================================================================================================
    private boolean endOfTokens () {

	return _index >= _tokens.size();

    } // endOfTokens ()
    // =============================================================================================================================



// =================================================================================================================================
} // class Parser
// =================================================================================================================================
