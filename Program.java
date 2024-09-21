// =================================================================================================================================
// IMPORTS

import java.util.List;
// =================================================================================================================================



// =================================================================================================================================
/**
 * A single program, which is a list expressions to evaluate.
 */
public class Program {
// =================================================================================================================================



    // =============================================================================================================================
    // DATA MEMBERS

    /** The expressions in order in which they appear in the source. */
    private List<Expression> _expressions;
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Create a new program from a given list of expressions.
     *
     * @param expressions A list of expressions.
     */
    public Program (List<Expression> expressions) {

	_expressions = expressions;

    } // Program ()
    // =============================================================================================================================



    // =============================================================================================================================
    /**
     * Generate assembly code that will execute this program.
     *
     * @return the generated assembly code.
     */
    public String toAssembly () {

	// Prologue stub code, setting up and starting the definition of main().
	String assembly = ( "\tglobal\tmain\n"     +
			    "\textern\tprintf\n\n" +
			    "\tsection\t.text\n\n" +
			    "main:\n\n"            );

	// Evaluate and print each expression.
	boolean firstpass = true;
	for (Expression expression : _expressions) {

	    // Prefix the code with a textual representing of this expression as a comment.
	    assembly += "; Expression: " + expression + '\n';
	    assembly += ";   Evaluate\n";

	    // Generate the assembly that evaluates the expression.
	    assembly += expression.toAssembly();

	    // Call printf() to emit the result of the expression, which is on top of the stack.
	    assembly += ";   Print\n";
	    if (firstpass) {
		assembly += ( ";     arg[0] (rdi) = formatting string\n" +
			      ";     arg[1] (rsi) = expression result\n" +
			      ";     arg[2] (rax) = end of varargs\n"    );
		firstpass = false;
	    }
	    assembly += ( "\tmov\trdi,\texpstr" + expression._position + '\n' +
			  "\tpop\trsi\n"                                      +
			  "\tmov\trax,\t0\n"                                  +
			  "\tcall\tprintf\n"                                  );
	    
	}

	// Epilogue stub code A: returning from main().
	assembly += "\n; Return from main()\n";
	assembly += "\tmov\trax,\t0\n";
	assembly += "\tret\n";

	// Epilogue stub code B: Define the string constants that are used during printing, namely the expressions themselves.
	assembly += "\n\tsection\t.data\n\n";
	for (Expression expression : _expressions) {

	    // Create a label and then define the string with an appended newline (10) and null termination.  Use the position value
	    // as a unique ID.  Each of these is a formatting string for printf that includes markers for the result (%d).
	    assembly += "expstr" + expression._position + ":\tdb\t\"" + expression + " = %d\", 10, 0\n";
	    
	}

	return assembly;

    } // toAssembly ()
    // =============================================================================================================================


    
    // =============================================================================================================================
    public String toString () {

	String text = "";
	for (Expression expression : _expressions) {
	    text += expression.toString() + '\n';
	}

	return text;
	
    } // toString ()
    // =============================================================================================================================
    
    
    
// =================================================================================================================================
} // class Program
// =================================================================================================================================
