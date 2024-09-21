// =================================================================================================================================
// IMPORTS

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// =================================================================================================================================



// =================================================================================================================================
/**
 * Perform lexical on an input, generating a sequence of <code>Token</code>s.
 */
public class Lexer {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The sequence of characters. */
    private List<Character> _source;

    /** The position within the source during a scan. */
    private int             _position;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a lexer for a particular input stream of characters.  Pre-read the full input, preparing it to be scanned.
     *
     * @param source The input character sequence that forms the source code.
     */
    public Lexer (List<Character> source) {

	_source   = source;
	_position = 0;

    } // Lexer ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Attempt to scan the sequence of characters into a sequence of tokens.
     *
     * @return A list of <code>Token</code>s found.
     */
    public List<Token> scan () {

	// Prepare to move through the character stream.
	List<Token> tokens = new ArrayList<Token>();

	// Scan the input.  Track positioning within the input.
	_position = 0;
	while (!endOfSource()) {

	    // Useful locals during scanning.
	    int     startPosition = -1;
	    boolean isHex         = false;

	    // Grab the next character.
	    char   c     = _source.get(_position);
	    String text  = "";
	    Token  token = null;

	    switch (c) {
		
	    // Whitespace?
	    case ' ':
	    case '\t':
	    case '\n':
		token = new Token(c, _position, Token.Type.WHITESPACE);
		break;
	    
	    // Open-closes?
	    case '(':
		token = new Token(_position, Token.Type.OPENPAREN);
		break;
	    case ')':
		token = new Token(_position, Token.Type.CLOSEPAREN);
		break;

	    // Operator?
	    case '+':
		token = new Token(_position, Token.Type.PLUS);
		break;
	    case '-':
		token = new Token(_position, Token.Type.DASH);
		break;
	    case '*':
		token = new Token(_position, Token.Type.STAR);
		break;
	    case '/':
		token = new Token(_position, Token.Type.SLASH);
		break;
	    case '%':
		token = new Token(_position, Token.Type.PERCENT);
		break;

	    // Comment?
	    case '#': {
		    
		// Read characters until the end-of-line is reached.
		startPosition = _position;
		while (true) {

		    _position += 1;
		    if (endOfSource()) {
			Utility.error("Input ended mid-comment", _position);
		    }
		    c = _source.get(_position);
		    if (c == '\n') {
			break;
		    }
		    
		}

		// No token to create.
		Utility.debug(2, "Comment scanned from @" + startPosition + " to @" + _position);
		break;
		
	    }

	    // Integer?
	    case '0':
	    case '1':
	    case '2':
	    case '3':
	    case '4':
	    case '5':
	    case '6':
	    case '7':
	    case '8':
	    case '9': {

		// SK: WRITE THIS CASE, SCANNING AN INTEGER AND CREATING A TOKEN FOR IT.

		startPosition = _position;
		while (_position < _source.size() && Character.isDigit(_source.get(_position))) {
		    _position ++;
		}
		text = "";
		for (int i = startPosition; i < _position; i++) {
		    text += _source.get(i);
		}

		int value = 0;
        for (char num : text.toCharArray()) {
            value = value * 10 + (num - '0');
        }

		token = new Token.IntToken(text, startPosition, value);

		_position--;

		break;
		
	    }
		
	    // Anything else is an error.
	    default:
		Utility.error("Malformed token", _position);

	    } // switch (c)
	    
	    // If a token was created (not a comment), add it to the list scanned.
	    if (token != null) {
		tokens.add(token);
	    }
	    _position += 1;

	} // while (!endOfSource())

	return tokens;

    } // scan ()
    // =============================================================================================================================



    // =============================================================================================================================
    private boolean endOfSource () {

	return _position >= _source.size();

    } // endOfSource ()
    // =============================================================================================================================



// =================================================================================================================================
} // class Lexer
// =================================================================================================================================
